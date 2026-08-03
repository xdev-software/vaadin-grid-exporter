/*
 * Copyright © 2022 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.xdev.vaadin.grid_exporter.jasper.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;

import software.xdev.dynamicreports.jasper.base.export.AbstractJasperExporter;
import software.xdev.dynamicreports.jasper.builder.JasperReportBuilder;
import software.xdev.dynamicreports.jasper.builder.export.AbstractJasperExporterBuilder;
import software.xdev.dynamicreports.report.builder.DynamicReports;
import software.xdev.dynamicreports.report.builder.column.Columns;
import software.xdev.dynamicreports.report.builder.column.TextColumnBuilder;
import software.xdev.dynamicreports.report.builder.component.Components;
import software.xdev.dynamicreports.report.builder.component.PageXofYBuilder;
import software.xdev.dynamicreports.report.builder.component.TextFieldBuilder;
import software.xdev.dynamicreports.report.datasource.DRDataSource;
import software.xdev.dynamicreports.report.exception.DRException;
import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.format.AbstractFormat;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.grid.GridDataExtractor;
import software.xdev.vaadin.grid_exporter.jasper.DynamicExporter;
import software.xdev.vaadin.grid_exporter.jasper.JasperGridReportStyles;
import software.xdev.vaadin.grid_exporter.jasper.config.encoding.EncodingConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.encoding.ExportEncoding;
import software.xdev.vaadin.grid_exporter.jasper.config.header.HeaderConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.highlight.HighlightConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.page.PageConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.title.TitleConfig;


public abstract class AbstractJasperReportFormat<B extends AbstractJasperExporterBuilder<B,
	? extends AbstractJasperExporter>>
	extends AbstractFormat
{
	protected final JasperGridReportStyles jasperGridReportStyles = new JasperGridReportStyles.Default();
	
	protected final boolean hasPages;
	
	protected final boolean hasStyle;
	
	protected final DynamicExporter<B> jasperReportBuilderTo;
	protected final Function<OutputStream, B> jasperExportBuilderSupplier;
	
	protected AbstractJasperReportFormat(
		final String nameToDisplay,
		final String fileSuffix,
		final String mimeType,
		final boolean hasPages,
		final boolean hasStyle,
		final DynamicExporter<B> jasperReportBuilderTo,
		final Function<OutputStream, B> jasperExportBuilderSupplier)
	{
		super(nameToDisplay, fileSuffix, mimeType);
		this.hasPages = hasPages;
		this.hasStyle = hasStyle;
		this.jasperReportBuilderTo = jasperReportBuilderTo;
		this.jasperExportBuilderSupplier = jasperExportBuilderSupplier;
	}
	
	@Override
	public <T> byte[] export(
		final GridDataExtractor<T> gridDataExtractor,
		final List<ColumnConfiguration<T>> columnsToExport,
		final List<? extends SpecificConfig> configs)
	{
		return this.exportToBytes(this.buildReport(gridDataExtractor, columnsToExport, configs), configs);
	}
	
	protected byte[] exportToBytes(
		final JasperReportBuilder reportBuilder,
		final List<? extends SpecificConfig> configs)
	{
		final ByteArrayOutputStream stream = new ByteArrayOutputStream(4096);
		
		this.getConfigFrom(configs, EncodingConfig.class)
			.filter(EncodingConfig::supportsAndUseBOM)
			.map(EncodingConfig::getSelected)
			.ifPresent(c -> {
				try
				{
					stream.write(c.bom().getBytes(c.charset()));
				}
				catch(final IOException ioe)
				{
					throw new UncheckedIOException(ioe);
				}
			});
		
		this.export(reportBuilder, this.getJasperExportBuilder(stream), configs);
		return stream.toByteArray();
	}
	
	protected B getJasperExportBuilder(final ByteArrayOutputStream stream)
	{
		return this.jasperExportBuilderSupplier.apply(stream);
	}
	
	@SuppressWarnings("java:S112")
	protected void export(
		final JasperReportBuilder reportBuilder,
		final B exportBuilder,
		final List<? extends SpecificConfig> configs)
	{
		this.getValueFrom(configs, EncodingConfig.class, EncodingConfig::getSelected)
			.map(ExportEncoding::charset)
			.map(Charset::name)
			.ifPresent(exportBuilder::setCharacterEncoding);
		
		try
		{
			this.jasperReportBuilderTo.export(reportBuilder, exportBuilder);
		}
		catch(final DRException e)
		{
			throw new RuntimeException("Failed to export", e);
		}
	}
	
	protected <T> JasperReportBuilder buildReport(
		final GridDataExtractor<T> gridDataExtractor,
		final List<ColumnConfiguration<T>> columnsToExport,
		final List<? extends SpecificConfig> configs)
	{
		final JasperReportBuilder report = DynamicReports.report();
		
		columnsToExport.stream()
			.map(this::toReportColumn)
			.forEach(report::addColumn);
		
		report.setDataSource(this.buildDataSource(gridDataExtractor, columnsToExport));
		
		if(this.hasStyle)
		{
			report.setColumnTitleStyle(this.jasperGridReportStyles.columnTitleStyle());
			report.setColumnStyle(this.jasperGridReportStyles.columnStyle());
		}
		
		report.setIgnorePagination(!this.hasPages);
		
		// Always required
		report.setReportName("Report");
		
		this.getConfigFrom(configs, TitleConfig.class)
			.filter(TitleConfig::notTitleEmpty)
			.map(TitleConfig::getTitle)
			.ifPresent(title ->
			{
				final TextFieldBuilder<String> txtTitle = Components.text(title);
				if(this.hasStyle)
				{
					txtTitle.setStyle(this.jasperGridReportStyles.titleStyle());
				}
				report.title(txtTitle);
				report.setReportName(title);
			});
		
		report.setShowColumnTitle(
			this.getValueFrom(configs, HeaderConfig.class, HeaderConfig::isExportHeader)
				.orElse(false));
		
		this.getValueFrom(configs, HighlightConfig.class, HighlightConfig::isHighlightOddRows)
			.filter(highlight -> highlight)
			.ifPresent(x ->
			{
				report.setDetailOddRowStyle(this.jasperGridReportStyles.columnStyleHighlighted());
				report.highlightDetailOddRows();
			});
		
		this.getConfigFrom(configs, PageConfig.class)
			.ifPresent(pc -> {
				report.setPageFormat(pc.getSelectedPageType(), pc.getSelectedPageOrientation());
				if(pc.isUsePageNumbering())
				{
					final PageXofYBuilder pageXofYBuilder = DynamicReports.cmp.pageXofY();
					if(this.hasStyle)
					{
						pageXofYBuilder.setStyle(this.jasperGridReportStyles.footerStyle());
					}
					report.pageFooter(pageXofYBuilder);
				}
				report.setPageMargin(DynamicReports.margin(pc.getPageMargin()));
			});
		
		return report;
	}
	
	protected <T> DRDataSource buildDataSource(
		final GridDataExtractor<T> gridDataExtractor,
		final List<ColumnConfiguration<T>> columnsToExport)
	{
		final DRDataSource dataSource = new DRDataSource(
			columnsToExport.stream()
				.map(ColumnConfiguration::getHeader)
				.toArray(String[]::new));
		
		gridDataExtractor.getSortedAndFilteredData(columnsToExport)
			.stream()
			.map(List::toArray)
			.forEach(dataSource::add);
		
		return dataSource;
	}
	
	protected TextColumnBuilder<String> toReportColumn(final ColumnConfiguration<?> column)
	{
		return Columns.column(column.getHeader(), column.getHeader(), String.class);
	}
}
