package software.xdev.vaadin.grid_exporter.jasper.format;

import java.io.OutputStream;
import java.util.function.Function;

import net.sf.dynamicreports.jasper.base.export.AbstractJasperExporter;
import net.sf.dynamicreports.jasper.builder.export.AbstractJasperExporterBuilder;
import software.xdev.vaadin.grid_exporter.jasper.DynamicExporter;
import software.xdev.vaadin.grid_exporter.jasper.config.header.HeaderConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.highlight.HighlightConfigComponent;


public abstract class AbstractJasperReportSpreadsheetFormat
	<B extends AbstractJasperExporterBuilder<B, ? extends AbstractJasperExporter>>
	extends AbstractJasperReportFormat<B>
{
	protected AbstractJasperReportSpreadsheetFormat(
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
			false,
			true,
			jasperReportBuilderTo,
			jasperExportBuilderSupplier);
		this.withConfigComponents(
			HeaderConfigComponent::new,
			HighlightConfigComponent::new
		);
	}
}
