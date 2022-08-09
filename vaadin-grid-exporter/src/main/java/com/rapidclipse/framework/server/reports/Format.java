package com.rapidclipse.framework.server.reports;

/*-
 * #%L
 * vaadin-grid-exporter
 * %%
 * Copyright (C) 2022 XDEV Software
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Objects;

import net.sf.dynamicreports.design.transformation.StyleResolver;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.style.DRFont;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;


/**
 * @author XDEV Software
 */
public interface Format
{
	String name();
	
	String fileSuffix();
	
	String mimeType();
	
	Exporter createExporter();
	
	/**
	 * Returns if a preview can be shown in a "standardized" browser (e.g. the latest versions of Chrome and Firefox)
	 *
	 * @return <code>true</code> if a preview can be shown in the browser
	 */
	boolean isPreviewableInStandardBrowser();
	
	/**
	 * Returns if a report is paginated (on every new page a new title, column-headers are printed, etc)
	 *
	 * @return <code>true</code> if the pagination is active
	 */
	boolean isPaginationActive();
	
	/**
	 * Returns if a report has a page margin.
	 *
	 * @return <code>true</code> if there is a page margin
	 */
	boolean hasPageMargin();
	
	static Format Csv()
	{
		return new Csv();
	}
	
	static Format Docx()
	{
		return new Docx();
	}
	
	static Format Ods()
	{
		return new Ods();
	}
	
	static Format Odt()
	{
		return new Odt();
	}
	
	static Format Pdf()
	{
		return new Pdf();
	}
	
	static Format Pptx()
	{
		return new Pptx();
	}
	
	static Format Rtf()
	{
		return new Rtf();
	}
	
	static Format Text()
	{
		return new Text();
	}
	
	static Format Xls()
	{
		return new Xls();
	}
	
	static Format Xlsx()
	{
		return new Xlsx();
	}
	
	static Format Xml()
	{
		return new Xml();
	}
	
	static Format Html()
	{
		return new Html();
	}
	
	/**
	 * Loads all default defined formats via {@link java.util.ServiceLoader}.
	 * The file with the definitions can be found in META-INF/services/.
	 *
	 * @return
	 */
	static Format[] All()
	{
		return new Format[]{
			Pdf(),
			Html(),
			Xml(),
			Text(),
			Rtf(),
			Csv(),
			Xls(),
			Xlsx(),
			Docx(),
			Pptx(),
			Odt(),
			Ods()
		};
	}
	
	abstract class Abstract implements Format
	{
		private final String name;
		private final String fileSuffix;
		private final String mimeType;
		protected boolean canBePreviewedInStandardBrowser = false;
		protected boolean paginationActive = false;
		protected boolean hasPageMargin = true;
		
		protected Abstract(final String name, final String fileSuffix, final String mimeType)
		{
			super();
			
			this.name = name;
			this.fileSuffix = fileSuffix;
			this.mimeType = mimeType;
		}
		
		public void canBePreviewedInStandardBrowser()
		{
			this.setPreviewableInStandardBrowser(true);
		}
		
		public void setPreviewableInStandardBrowser(final boolean canBePreviewedInStandardBrowser)
		{
			this.canBePreviewedInStandardBrowser = canBePreviewedInStandardBrowser;
		}
		
		public void enablePagination()
		{
			this.setPaginationActive(true);
		}
		
		public void setPaginationActive(final boolean paginationActive)
		{
			this.paginationActive = paginationActive;
		}
		
		public void disablePageMargin()
		{
			this.setHasPageMargin(false);
		}
		
		public void setHasPageMargin(final boolean hasPageMargin)
		{
			this.hasPageMargin = hasPageMargin;
		}
		
		@Override
		public String name()
		{
			return this.name;
		}
		
		@Override
		public String fileSuffix()
		{
			return this.fileSuffix;
		}
		
		@Override
		public String mimeType()
		{
			return this.mimeType;
		}
		
		@Override
		public boolean isPreviewableInStandardBrowser()
		{
			return this.canBePreviewedInStandardBrowser;
		}
		
		@Override
		public boolean isPaginationActive()
		{
			return this.paginationActive;
		}
		
		@Override
		public boolean hasPageMargin()
		{
			return this.hasPageMargin;
		}
		
		@Override
		public Exporter createExporter()
		{
			return Exporter.New(this, this.createDynamicExporter(), this.createPlainExporter());
		}
		
		protected abstract DynamicExporter createDynamicExporter();
		
		protected abstract PlainExporter createPlainExporter();
		
		@Override
		public int hashCode()
		{
			return Objects.hash(this.name);
		}
		
		@Override
		public boolean equals(final Object obj)
		{
			if(this == obj)
			{
				return true;
			}
			if(obj == null)
			{
				return false;
			}
			if(this.getClass() != obj.getClass())
			{
				return false;
			}
			final Abstract other = (Abstract)obj;
			return Objects.equals(this.name, other.name);
		}
	}
	
	
	class Pdf extends Abstract
	{
		public static final String MIME_TYPE = "application/pdf";
		
		public Pdf()
		{
			super("PDF", "pdf", MIME_TYPE);
			this.canBePreviewedInStandardBrowser();
			this.enablePagination();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toPdf;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return JasperExportManager::exportReportToPdfStream;
		}
	}
	
	
	class Html extends Abstract
	{
		public static final String MIME_TYPE = "text/html";
		
		public Html()
		{
			super("HTML", "html", MIME_TYPE);
			this.canBePreviewedInStandardBrowser();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toHtml;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final HtmlExporter exporter = new HtmlExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleHtmlExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
	
	
	class Xml extends Abstract
	{
		public static final String MIME_TYPE = "text/xml";
		
		public Xml()
		{
			super("XML", "xml", MIME_TYPE);
			this.disablePageMargin();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toXml;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return JasperExportManager::exportReportToXmlStream;
		}
	}
	
	
	class Text extends Abstract
	{
		public static final String MIME_TYPE = "text/plain";
		
		public Text()
		{
			super("Text", "txt", MIME_TYPE);
			this.canBePreviewedInStandardBrowser();
			this.disablePageMargin();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toText;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JRTextExporter exporter = new JRTextExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleWriterExporterOutput(stream));
				final SimpleTextReportConfiguration configuration = new SimpleTextReportConfiguration();
				final DRFont font = Defaults.getDefaults().getFont();
				configuration.setCharWidth((float)StyleResolver.getFontWidth(font));
				configuration.setCharHeight((float)StyleResolver.getFontHeight(font));
				exporter.setConfiguration(configuration);
				exporter.exportReport();
			};
		}
	}
	
	
	class Rtf extends Abstract
	{
		public static final String MIME_TYPE = "text/rtf";
		
		public Rtf()
		{
			super("Rich Text", "rtf", MIME_TYPE);
			this.enablePagination();
			this.disablePageMargin();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toRtf;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JRRtfExporter exporter = new JRRtfExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleWriterExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
	
	
	class Csv extends Abstract
	{
		public static final String MIME_TYPE = "text/comma-separated-values";
		
		public Csv()
		{
			super("CSV", "csv", MIME_TYPE);
			this.disablePageMargin();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toCsv;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JRCsvExporter exporter = new JRCsvExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleWriterExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
	
	
	class Xls extends Abstract
	{
		public static final String MIME_TYPE = "application/vnd.ms-excel";
		
		public Xls()
		{
			super("Excel (xls)", "xls", MIME_TYPE);
			this.disablePageMargin();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toXls;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JRXlsExporter exporter = new JRXlsExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
	
	
	class Xlsx extends Abstract
	{
		public static final String MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		
		public Xlsx()
		{
			super("Excel (xlsx)", "xlsx", MIME_TYPE);
			this.disablePageMargin();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toXlsx;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JRXlsxExporter exporter = new JRXlsxExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
	
	
	class Docx extends Abstract
	{
		public static final String MIME_TYPE =
			"application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		
		public Docx()
		{
			super("Word (docx)", "docx", MIME_TYPE);
			this.enablePagination();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toDocx;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JRDocxExporter exporter = new JRDocxExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
	
	
	class Pptx extends Abstract
	{
		public static final String MIME_TYPE =
			"application/vnd.openxmlformats-officedocument.presentationml.presentation";
		
		public Pptx()
		{
			super("Powerpoint (pptx)", "pptx", MIME_TYPE);
			this.enablePagination();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toPptx;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JRPptxExporter exporter = new JRPptxExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
	
	
	class Odt extends Abstract
	{
		public static final String MIME_TYPE = "application/vnd.oasis.opendocument.text";
		
		public Odt()
		{
			super("Open Document Text (odt)", "odt", MIME_TYPE);
			this.enablePagination();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toOdt;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JROdtExporter exporter = new JROdtExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
	
	
	class Ods extends Abstract
	{
		public static final String MIME_TYPE = "application/vnd.oasis.opendocument.spreadsheet";
		
		public Ods()
		{
			super("Open Document Spreadsheet (ods)", "ods", MIME_TYPE);
			this.disablePageMargin();
		}
		
		@Override
		protected DynamicExporter createDynamicExporter()
		{
			return JasperReportBuilder::toOds;
		}
		
		@Override
		protected PlainExporter createPlainExporter()
		{
			return (print, stream) ->
			{
				final JROdsExporter exporter = new JROdsExporter(
					DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			};
		}
	}
}
