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
package software.xdev.vaadin.grid_exporter.jasper.config.header;

import com.vaadin.flow.component.checkbox.Checkbox;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


public class HeaderConfigComponent extends SpecificConfigComponent<HeaderConfig>
{
	protected final Checkbox chbxExportHeader = new Checkbox();
	
	public HeaderConfigComponent(final Translator translator, final boolean defaultExportHeaderValue)
	{
		super(translator, () -> new HeaderConfig(defaultExportHeaderValue), JasperConfigsLocalization.HEADER);
		
		this.initUI();
		
		this.registerBindings();
	}
	
	public HeaderConfigComponent(final Translator translator)
	{
		this(translator, true);
	}
	
	protected void initUI()
	{
		this.chbxExportHeader.setLabel(this.translate(JasperConfigsLocalization.EXPORT_HEADER));
		
		this.getContent().add(this.chbxExportHeader);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.chbxExportHeader)
			.bind(HeaderConfig::isExportHeader, HeaderConfig::setExportHeader);
	}
}
