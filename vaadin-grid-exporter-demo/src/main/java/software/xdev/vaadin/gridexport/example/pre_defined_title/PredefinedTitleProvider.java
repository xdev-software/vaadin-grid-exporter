package software.xdev.vaadin.gridexport.example.pre_defined_title;

import java.util.List;

import software.xdev.dynamicreports.jasper.builder.JasperReportBuilder;
import software.xdev.dynamicreports.jasper.builder.export.Exporters;
import software.xdev.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import software.xdev.vaadin.grid_exporter.GridExporterProvider;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;
import software.xdev.vaadin.grid_exporter.jasper.config.header.HeaderConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.highlight.HighlightConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.page.PageConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.title.TitleConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.title.TitleConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.format.AbstractJasperReportFormat;


public class PredefinedTitleProvider extends GridExporterProvider
{
	public PredefinedTitleProvider(final String predefinedTitle)
	{
		super(
			JasperConfigsLocalization.DEFAULT_VALUES,
			List.of(
				new PredefinedTitlePdfFormat(predefinedTitle)
			));
	}
	
	public static class PredefinedTitlePdfFormat extends AbstractJasperReportFormat<JasperPdfExporterBuilder>
	{
		public PredefinedTitlePdfFormat(final String defaultTitle)
		{
			super(
				"PDF",
				"pdf",
				"application/pdf",
				true,
				true,
				JasperReportBuilder::toPdf,
				Exporters::pdfExporter
			);
			this.withConfigComponents(
				translator -> new PreDefinedTitleConfigComponent(translator, defaultTitle),
				HeaderConfigComponent::new,
				HighlightConfigComponent::new,
				PageConfigComponent::new
			);
		}
	}
	
	
	public static class PreDefinedTitleConfigComponent extends TitleConfigComponent
	{
		public PreDefinedTitleConfigComponent(final Translator translator, final String defaultTitle)
		{
			super(translator);
			this.setNewConfigSupplier(() -> {
				final TitleConfig titleConfig = new TitleConfig();
				titleConfig.setTitle(defaultTitle);
				return titleConfig;
			});
		}
	}
}
