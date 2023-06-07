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

import java.io.OutputStream;
import java.util.function.Function;

import software.xdev.dynamicreports.jasper.base.export.AbstractJasperExporter;
import software.xdev.dynamicreports.jasper.builder.export.AbstractJasperExporterBuilder;
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
