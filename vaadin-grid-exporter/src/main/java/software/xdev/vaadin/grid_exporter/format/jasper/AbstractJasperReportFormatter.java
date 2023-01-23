package software.xdev.vaadin.grid_exporter.format.jasper;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import software.xdev.vaadin.grid_exporter.DynamicExporter;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.format.GeneralConfig;
import software.xdev.vaadin.grid_exporter.format.GridDataSourceFactory;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.grid.column.ColumnConfiguration;


public abstract class AbstractJasperReportFormatter<T, E extends SpecificConfig> implements Format<T, E>
{
	
	private final GridDataSourceFactory gridDataSourceFactory = GridDataSourceFactory.New();
	private final GridReportStyles gridReportStyles = GridReportStyles.New();
	
	private final DynamicExporter exporter;
	
	private final String nameToDisplay;
	private final String fileSuffix;
	private final String mimeType;
	private final Translator translator;
	
	/**
	 * Returns if a report is paginated (on every new page a new title, column-headers are printed, etc)
	 *
	 * @return <code>true</code> if the pagination is active
	 */
	public abstract boolean isPaginationActive();
	
	public AbstractJasperReportFormatter(
		final DynamicExporter exporter,
		final String nameToDisplay,
		final String fileSuffix,
		final String mimeType,
		final Translator translator)
	{
		this.exporter = exporter;
		this.nameToDisplay = nameToDisplay;
		this.fileSuffix = fileSuffix;
		this.mimeType = mimeType;
		this.translator = translator;
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
	
	private OutputStream exportToBytes(final JasperReportBuilder reportBuilder)
	{
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		this.export(reportBuilder, stream);
		return stream;
	}
	
	@Override
	public OutputStream export(
		final Grid<T> gridToExport,
		final GeneralConfig<T> generalConfig,
		final E specificConfig)
	{
		return this.exportToBytes(this.buildReport(gridToExport, generalConfig, specificConfig));
	}
	
	private TextColumnBuilder<String> toReportColumn(final ColumnConfiguration<T> column)
	{
		final TextColumnBuilder<String> reportColumn =
			Columns.column(column.getHeader(), column.getKeyOrHeader(), String.class);
		
		final Integer width = column.getColumnWidth();
		if(width != null && width > 0)
		{
			reportColumn.setFixedWidth(width);
		}
		
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
	
	protected JasperReportBuilder buildReport(
		final Grid<T> gridToExport,
		final GeneralConfig<T> configuration,
		final E specificConfig)
	{
		final JasperReportBuilder report = DynamicReports.report();
		
		configuration.getColumnConfigurations().stream()
			.filter(ColumnConfiguration::isVisible)
			.map(this::toReportColumn)
			.forEach(report::addColumn);
		
		report.setColumnTitleStyle(this.gridReportStyles.columnTitleStyle());
		report.setColumnStyle(this.gridReportStyles.columnStyle());
		
		final String title = configuration.getTitle();
		if(!StringUtils.isEmpty(title))
		{
			report.title(Components.text(title).setStyle(this.gridReportStyles.titleStyle()));
			report.setReportName(title);
		}
		else
		{
			report.setReportName("GridExport");
		}
		
		report.setIgnorePagination(!this.isPaginationActive());
		
		return report;
	}
	
}
