/*
 * Copyright © 2022 XDEV Software (https://xdev.software/en)
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
package software.xdev.vaadin.grid_exporter;



import java.util.Objects;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;


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
			return Exporter.New(this, this.createDynamicExporter());
		}
		
		protected abstract DynamicExporter createDynamicExporter();
		
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
	}
}
