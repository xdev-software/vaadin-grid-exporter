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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.vaadin.flow.server.StreamResource;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;


public interface Exporter
{
	StreamResource exportToResource(
		final JasperReportBuilder reportBuilder,
		final String fileNamePrefix);
	
	byte[] exportToBytes(final JasperReportBuilder reportBuilder);
	
	void export(final JasperReportBuilder reportBuilder, final OutputStream stream);
	
	static Exporter New(
		final Format format,
		final DynamicExporter dynamicExporter)
	{
		return new Default(format, dynamicExporter);
	}
	
	class Default implements Exporter
	{
		private final Format format;
		private final DynamicExporter dynamicExporter;
		
		protected Default(
			final Format format,
			final DynamicExporter dynamicExporter)
		{
			super();
			
			this.format          = format;
			this.dynamicExporter = dynamicExporter;
		}
		
		protected StreamResource createResource(final byte[] bytes, final String fileNamePrefix)
		{
			final String         fileName = fileNamePrefix != null && !fileNamePrefix.trim().isEmpty()
				? fileNamePrefix
				: this.getDefaultFileNamePrefix();
			
			final StreamResource resource = new StreamResource(
				fileName + "." + this.format.fileSuffix(),
				() -> new ByteArrayInputStream(bytes));
			resource.setContentType(this.format.mimeType());
			return resource;
		}
		
		protected String getDefaultFileNamePrefix()
		{
			return "report" + System.currentTimeMillis();
		}
		
		
		@Override
		public StreamResource exportToResource(
			final JasperReportBuilder reportBuilder,
			final String fileNamePrefix)
		{
			return this.createResource(this.exportToBytes(reportBuilder), fileNamePrefix);
		}
		
		@Override
		public byte[] exportToBytes(final JasperReportBuilder reportBuilder)
		{
			final ByteArrayOutputStream stream = new ByteArrayOutputStream();
			this.export(reportBuilder, stream);
			return stream.toByteArray();
		}
		
		@Override
		public void export(final JasperReportBuilder reportBuilder, final OutputStream stream)
		{
			try
			{
				this.dynamicExporter.export(reportBuilder, stream);
			}
			catch(final DRException e)
			{
				throw new ReportException(e);
			}
		}
	}
}
