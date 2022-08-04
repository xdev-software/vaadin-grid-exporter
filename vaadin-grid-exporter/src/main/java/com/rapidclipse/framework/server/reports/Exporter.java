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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.vaadin.flow.server.StreamResource;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public interface Exporter
{
	StreamResource exportToResource(final JasperReportBuilder reportBuilder);
	
	StreamResource exportToResource(
		final JasperReportBuilder reportBuilder,
		final String fileNamePrefix);
	
	StreamResource exportToResource(
		final InputStream jrxml,
		final JRDataSource dataSource,
		final Map<String, Object> parameters);
	
	StreamResource exportToResource(
		final InputStream jrxml,
		final JRDataSource dataSource,
		final Map<String, Object> parameters,
		final String fileNamePrefix);
	
	byte[] exportToBytes(final JasperReportBuilder reportBuilder);
	
	byte[] exportToBytes(
		final InputStream jrxml,
		final JRDataSource dataSource,
		final Map<String, Object> parameters);
	
	void export(final JasperReportBuilder reportBuilder, final OutputStream stream);
	
	void export(final JasperPrint print, final OutputStream stream);
	
	void export(
		final InputStream jrxml,
		final JRDataSource dataSource,
		final Map<String, Object> parameters,
		final OutputStream stream);
	
	public static Exporter New(
		final Format format,
		final DynamicExporter dynamicExporter,
		final PlainExporter plainExporter)
	{
		return new Default(format, dynamicExporter, plainExporter);
	}
	
	public static class Default implements Exporter
	{
		private final Format          format;
		private final DynamicExporter dynamicExporter;
		private final PlainExporter   plainExporter;
		
		protected Default(
			final Format format,
			final DynamicExporter dynamicExporter,
			final PlainExporter plainExporter)
		{
			super();
			
			this.format          = format;
			this.dynamicExporter = dynamicExporter;
			this.plainExporter   = plainExporter;
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
		public StreamResource exportToResource(final JasperReportBuilder reportBuilder)
		{
			return this.exportToResource(reportBuilder, this.getDefaultFileNamePrefix());
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
		
		@Override
		public StreamResource exportToResource(
			final InputStream jrxml,
			final JRDataSource dataSource,
			final Map<String, Object> parameters)
		{
			return this.exportToResource(jrxml, dataSource, parameters, this.getDefaultFileNamePrefix());
		}
		
		@Override
		public StreamResource exportToResource(
			final InputStream jrxml,
			final JRDataSource dataSource,
			final Map<String, Object> parameters,
			final String fileNamePrefix)
		{
			return this.createResource(this.exportToBytes(jrxml, dataSource, parameters), fileNamePrefix);
		}
		
		@Override
		public byte[] exportToBytes(
			final InputStream jrxml,
			final JRDataSource dataSource,
			final Map<String, Object> parameters)
		{
			final ByteArrayOutputStream stream = new ByteArrayOutputStream();
			this.export(jrxml, dataSource, parameters, stream);
			return stream.toByteArray();
		}
		
		@Override
		public void export(
			final InputStream jrxml,
			final JRDataSource dataSource,
			final Map<String, Object> parameters,
			final OutputStream stream)
		{
			try
			{
				final JasperReport report = JasperCompileManager.compileReport(jrxml);
				final JasperPrint  print  = JasperFillManager.fillReport(report, parameters,
					dataSource);
				this.export(print, stream);
			}
			catch(final JRException e)
			{
				throw new ReportException(e);
			}
		}
		
		@Override
		public void export(final JasperPrint print, final OutputStream stream)
		{
			try
			{
				this.plainExporter.export(print, stream);
			}
			catch(final JRException e)
			{
				throw new ReportException(e);
			}
		}
	}
}
