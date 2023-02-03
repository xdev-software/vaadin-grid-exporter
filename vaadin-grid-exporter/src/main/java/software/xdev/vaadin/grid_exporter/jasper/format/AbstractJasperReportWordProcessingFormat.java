package software.xdev.vaadin.grid_exporter.jasper.format;

import java.io.OutputStream;
import java.util.function.Function;

import net.sf.dynamicreports.jasper.base.export.AbstractJasperExporter;
import net.sf.dynamicreports.jasper.builder.export.AbstractJasperExporterBuilder;
import software.xdev.vaadin.grid_exporter.jasper.DynamicExporter;
import software.xdev.vaadin.grid_exporter.jasper.config.header.HeaderConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.highlight.HighlightConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.page.PageConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.title.TitleConfigComponent;


public abstract class AbstractJasperReportWordProcessingFormat
	<B extends AbstractJasperExporterBuilder<B, ? extends AbstractJasperExporter>>
	extends AbstractJasperReportFormat<B>
{
	protected AbstractJasperReportWordProcessingFormat(
		final String nameToDisplay,
		final String fileSuffix,
		final String mimeType,
		final DynamicExporter<B> jasperReportBuilderTo,
		final Function<OutputStream, B> jasperExportBuilderSupplier)
	{
		super(
			nameToDisplay,
			fileSuffix,
			mimeType,
			true,
			true,
			jasperReportBuilderTo,
			jasperExportBuilderSupplier);
		this.withConfigComponents(
			TitleConfigComponent::new,
			HeaderConfigComponent::new,
			HighlightConfigComponent::new,
			PageConfigComponent::new
		);
	}
}
