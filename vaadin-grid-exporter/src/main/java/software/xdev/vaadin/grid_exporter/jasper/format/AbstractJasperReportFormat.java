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
import java.util.Optional;
import java.util.function.Function;

import com.vaadin.flow.component.grid.Grid;

import net.sf.dynamicreports.jasper.base.export.AbstractJasperExporter;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.AbstractJasperExporterBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.PageXofYBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.format.AbstractFormat;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.jasper.DynamicExporter;
import software.xdev.vaadin.grid_exporter.jasper.GridDataSourceFactory;
import software.xdev.vaadin.grid_exporter.jasper.GridReportStyles;
import software.xdev.vaadin.grid_exporter.jasper.config.encoding.EncodingConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.encoding.ExportEncoding;
import software.xdev.vaadin.grid_exporter.jasper.config.header.HeaderConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.highlight.HighlightConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.page.PageConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.title.TitleConfig;


public abstract class AbstractJasperReportFormat
	<B extends AbstractJasperExporterBuilder<B, ? extends AbstractJasperExporter>>
	extends AbstractFormat
{
	protected final GridReportStyles gridReportStyles = GridReportStyles.New();
	
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
		final Grid<T> gridToExport,
		final List<ColumnConfiguration<T>> columnsToExport,
		final List<? extends SpecificConfig> configs)
	{
		return this.exportToBytes(this.buildReport(gridToExport, columnsToExport, configs), configs);
	}
	
	protected byte[] exportToBytes(
		final JasperReportBuilder reportBuilder,
		final List<? extends SpecificConfig> configs)
	{
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
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
		final Grid<T> gridToExport,
		final List<ColumnConfiguration<T>> columnsToExport,
		final List<? extends SpecificConfig> configs)
	{
		final JasperReportBuilder report = DynamicReports.report();
		
		columnsToExport.stream()
			.map(this::toReportColumn)
			.forEach(report::addColumn);
		
		report.setDataSource(new GridDataSourceFactory.Default<T>().createDataSource(gridToExport, columnsToExport));
		
		if(this.hasStyle)
		{
			report.setColumnTitleStyle(this.gridReportStyles.columnTitleStyle());
			report.setColumnStyle(this.gridReportStyles.columnStyle());
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
					txtTitle.setStyle(this.gridReportStyles.titleStyle());
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
				report.setDetailOddRowStyle(this.gridReportStyles.columnStyleHighlighted());
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
						pageXofYBuilder.setStyle(this.gridReportStyles.footerStyle());
					}
					report.pageFooter(pageXofYBuilder);
				}
				report.setPageMargin(DynamicReports.margin(pc.getPageMargin()));
			});
		
		return report;
	}
	
	protected TextColumnBuilder<String> toReportColumn(final ColumnConfiguration<?> column)
	{
		return Columns.column(column.getHeader(), column.getKeyOrHeader(), String.class);
	}
	
	protected <C extends SpecificConfig> Optional<C> getConfigFrom(
		final List<? extends SpecificConfig> configs,
		final Class<C> targetedConfigClass)
	{
		return configs.stream()
			.filter(targetedConfigClass::isInstance)
			.map(targetedConfigClass::cast)
			.findFirst();
	}
	
	protected <V, C extends SpecificConfig> Optional<V> getValueFrom(
		final List<? extends SpecificConfig> configs,
		final Class<C> targetedConfigClass,
		final Function<C, V> mapper)
	{
		return this.getConfigFrom(configs, targetedConfigClass)
			.stream()
			.findFirst()
			.map(mapper);
	}
}
