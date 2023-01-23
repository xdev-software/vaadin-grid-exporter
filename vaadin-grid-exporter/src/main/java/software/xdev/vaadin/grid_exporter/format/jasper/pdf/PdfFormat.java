package software.xdev.vaadin.grid_exporter.format.jasper.pdf;

import com.vaadin.flow.component.grid.Grid;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.FormatConfigComponent;
import software.xdev.vaadin.grid_exporter.format.GeneralConfig;
import software.xdev.vaadin.grid_exporter.format.GridDataSourceFactory;
import software.xdev.vaadin.grid_exporter.format.jasper.AbstractJasperReportFormatter;
import software.xdev.vaadin.grid_exporter.format.jasper.GridReportStyles;


public class PdfFormat<T> extends AbstractJasperReportFormatter<T, PdfSpecificConfig>
{
	
	public PdfFormat(final Translator translator)
	{
		super(JasperReportBuilder::toPdf, "PDF", "pdf", "application/pdf", translator);
	}
	
	@Override
	public boolean isPreviewableInStandardBrowser()
	{
		return true;
	}
	
	@Override
	public FormatConfigComponent<PdfSpecificConfig> getConfigurationComponent()
	{
		return new PdfFormatComponent();
	}
	
	@Override
	public boolean isPaginationActive()
	{
		return true;
	}
	
	@Override
	public boolean hasPageMargin()
	{
		return false;
	}
	
	@Override
	protected JasperReportBuilder buildReport(
		final Grid<T> gridToExport,
		final GeneralConfig<T> generalConfig,
		final PdfSpecificConfig specificConfig)
	{
		final JasperReportBuilder report = super.buildReport(gridToExport, generalConfig, specificConfig);
		
		if(specificConfig.isShowPageNumber())
		{
			report.pageFooter(DynamicReports.cmp.pageXofY().setStyle(GridReportStyles.New().footerStyle()));
		}
		
		if(specificConfig.isHighlightRows())
		{
			report.highlightDetailOddRows();
		}
		
		report.setShowColumnTitle(true);
		report.setDataSource(new GridDataSourceFactory.Default().createDataSource(gridToExport, generalConfig));
		report.setPageFormat(specificConfig.getPageType(), specificConfig.getPageOrientation());
		
		report.setPageMargin(DynamicReports.margin(20));
		
		return report;
	}
}
