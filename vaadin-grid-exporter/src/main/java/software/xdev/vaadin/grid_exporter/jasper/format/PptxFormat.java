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

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.builder.export.JasperPptxExporterBuilder;
import software.xdev.vaadin.grid_exporter.jasper.config.header.HeaderConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.highlight.HighlightConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.page.PageConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.title.TitleConfigComponent;


public class PptxFormat extends AbstractJasperReportFormat<JasperPptxExporterBuilder>
{
	public PptxFormat()
	{
		super(
			"Powerpoint (pptx)",
			"pptx",
			"application/vnd.openxmlformats-officedocument.presentationml.presentation",
			true,
			true,
			JasperReportBuilder::toPptx,
			Exporters::pptxExporter
		);
		this.withConfigComponents(
			TitleConfigComponent::new,
			HeaderConfigComponent::new,
			HighlightConfigComponent::new,
			PageConfigComponent::new
		);
	}
}
