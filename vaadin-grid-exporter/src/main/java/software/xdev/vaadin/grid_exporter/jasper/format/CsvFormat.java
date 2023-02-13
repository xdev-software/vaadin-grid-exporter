/*
 * Copyright © 2022 XDEV Software (https://xdev.software)
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
package software.xdev.vaadin.grid_exporter.jasper.format;

import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.encoding.EncodingConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.header.HeaderConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.separator.CSVSeparatorConfig;
import software.xdev.vaadin.grid_exporter.jasper.config.separator.CSVSeparatorConfigComponent;


public class CsvFormat extends AbstractJasperReportFormat<JasperCsvExporterBuilder>
{
	
	public CsvFormat()
	{
		super(
			"CSV",
			"csv",
			"text/comma-separated-value",
			false,
			false,
			JasperReportBuilder::toCsv,
			Exporters::csvExporter
		);
		this.withConfigComponents(
			HeaderConfigComponent::new,
			CSVSeparatorConfigComponent::new,
			EncodingConfigComponent::new
		);
	}
	
	@Override
	protected void export(
		final JasperReportBuilder reportBuilder,
		final JasperCsvExporterBuilder exportBuilder,
		final List<? extends SpecificConfig> configs)
	{
		this.getValueFrom(configs, CSVSeparatorConfig.class, CSVSeparatorConfig::getSeparator)
			.ifPresent(exportBuilder::setFieldDelimiter);
		
		super.export(reportBuilder, exportBuilder, configs);
	}
}
