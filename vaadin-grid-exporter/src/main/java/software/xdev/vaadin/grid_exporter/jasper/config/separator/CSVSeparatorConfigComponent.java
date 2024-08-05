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
package software.xdev.vaadin.grid_exporter.jasper.config.separator;

import com.vaadin.flow.component.textfield.TextField;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


public class CSVSeparatorConfigComponent extends SpecificConfigComponent<CSVSeparatorConfig>
{
	protected final TextField txtSeparator = new TextField();
	
	public CSVSeparatorConfigComponent(final Translator translator)
	{
		super(translator, CSVSeparatorConfig::new, JasperConfigsLocalization.SEPARATOR);
		
		this.initUIs();
		
		this.registerBindings();
	}
	
	protected void initUIs()
	{
		this.getContent().add(this.txtSeparator);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.txtSeparator)
			.asRequired()
			.bind(CSVSeparatorConfig::getSeparator, CSVSeparatorConfig::setSeparator);
	}
}
