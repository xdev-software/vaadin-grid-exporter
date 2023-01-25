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
package software.xdev.vaadin.grid_exporter.format.jasper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.format.GeneralConfig;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.format.jasper.pdf.PdfSpecificConfig;
import software.xdev.vaadin.grid_exporter.grid.column.ColumnConfiguration;


public abstract class AbstractJasperReportFormat<T, E extends SpecificConfig> implements Format<T, E>
{
	private final GridReportStyles gridReportStyles = GridReportStyles.New();
	private final DynamicExporter exporter;
	private final String nameToDisplay;
	private final String fileSuffix;
	private final String mimeType;
	private final Translator translator;
	private final boolean isPaginationActive;
	
	/**
	 * Returns if a report is paginated (on every new page a new title, column-headers are printed, etc)
	 *
	 * @return <code>true</code> if the pagination is active
	 */
	public boolean isPaginationActive()
	{
		return this.isPaginationActive;
	}
	
	protected AbstractJasperReportFormat(
		final DynamicExporter exporter,
		final String nameToDisplay,
		final String fileSuffix,
		final String mimeType,
		final boolean isPaginationActive,
		final Translator translator)
	{
		this.exporter = exporter;
		this.nameToDisplay = nameToDisplay;
		this.fileSuffix = fileSuffix;
		this.mimeType = mimeType;
		this.isPaginationActive = isPaginationActive;
		this.translator = translator;
	}
	
	public Translator getTranslator()
	{
		return this.translator;
	}
	
	@Override
	public String getFormatNameToDisplay()
	{
		return this.nameToDisplay;
	}
	
	@Override
	public String getFormatFilenameSuffix()
	{
		return this.fileSuffix;
	}
	
	@Override
	public String getMimeType()
	{
		return this.mimeType;
	}
	
	private byte[] exportToBytes(final JasperReportBuilder reportBuilder) throws DRException
	{
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		this.exporter.export(reportBuilder, stream);
		return stream.toByteArray();
	}
	
	@Override
	public byte[] export(
		final Grid<T> gridToExport,
		final GeneralConfig<T> generalConfig,
		final E specificConfig) throws IOException
	{
		try
		{
			return this.exportToBytes(this.buildReport(gridToExport, generalConfig, specificConfig));
		}
		catch(final DRException e)
		{
			throw new IOException(e);
		}
	}
	
	private TextColumnBuilder<String> toReportColumn(final ColumnConfiguration<T> column)
	{
		final TextColumnBuilder<String> reportColumn =
			Columns.column(column.getHeader(), column.getKeyOrHeader(), String.class);
		
		reportColumn.setHorizontalTextAlignment(
			this.toReportTextAlignment(column.getColumnAlignment()));
		
		return reportColumn;
	}
	
	private HorizontalTextAlignment toReportTextAlignment(final ColumnTextAlign alignment)
	{
		switch(alignment)
		{
			case END:
				return HorizontalTextAlignment.RIGHT;
			
			case CENTER:
				return HorizontalTextAlignment.CENTER;
			
			default:
				return HorizontalTextAlignment.LEFT;
		}
	}
	
	/**
	 * @param specificConfig contains all configuration for a specific format. Can be used in overriding methods (see
	 *                       {@link software.xdev.vaadin.grid_exporter.format.jasper.pdf.PdfFormat#buildReport(Grid,
	 *                       GeneralConfig, PdfSpecificConfig)}).
	 */
	protected JasperReportBuilder buildReport(
		final Grid<T> gridToExport,
		final GeneralConfig<T> generalConfig,
		final E specificConfig)
	{
		final JasperReportBuilder report = DynamicReports.report();
		
		generalConfig.getColumnConfigurations().stream()
			.filter(ColumnConfiguration::isVisible)
			.map(this::toReportColumn)
			.forEach(report::addColumn);
		
		report.setColumnTitleStyle(this.gridReportStyles.columnTitleStyle());
		report.setColumnStyle(this.gridReportStyles.columnStyle());
		report.setIgnorePagination(!this.isPaginationActive());
		report.setDataSource(new GridDataSourceFactory.Default<T>().createDataSource(gridToExport, generalConfig));
		return report;
	}
	
	public GridReportStyles getGridReportStyles()
	{
		return this.gridReportStyles;
	}
}
