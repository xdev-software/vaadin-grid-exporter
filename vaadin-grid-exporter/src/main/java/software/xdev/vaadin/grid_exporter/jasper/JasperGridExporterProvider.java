package software.xdev.vaadin.grid_exporter.jasper;

import java.util.Arrays;

import software.xdev.vaadin.grid_exporter.GridExporterProvider;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;
import software.xdev.vaadin.grid_exporter.jasper.format.CsvFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.DocxFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.HtmlFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.OdsFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.OdtFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.PdfFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.PptxFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.RtfFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.TextFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.XlsxFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.XmlFormat;


public class JasperGridExporterProvider extends GridExporterProvider
{
	public JasperGridExporterProvider()
	{
		super(
			JasperConfigsLocalization.DEFAULT_VALUES,
			Arrays.asList(
				new PdfFormat(),
				new XlsxFormat(),
				new CsvFormat(),
				new DocxFormat(),
				new HtmlFormat(),
				new OdsFormat(),
				new OdtFormat(),
				new PptxFormat(),
				new RtfFormat(),
				new TextFormat(),
				new XmlFormat()
			));
	}
}
