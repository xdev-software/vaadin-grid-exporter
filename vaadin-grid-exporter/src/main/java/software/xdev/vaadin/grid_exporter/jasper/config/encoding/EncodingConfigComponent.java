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
package software.xdev.vaadin.grid_exporter.jasper.config.encoding;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


public class EncodingConfigComponent extends SpecificConfigComponent<EncodingConfig>
{
	protected final ComboBox<ExportEncoding> cbExportEncoding = new ComboBox<>();
	
	protected final Checkbox chbxUseBom = new Checkbox();
	
	public EncodingConfigComponent(final Translator translator)
	{
		super(translator, EncodingConfig::new, JasperConfigsLocalization.ENCODING);
		
		this.initUI();
		
		this.registerBindings();
	}
	
	protected void initUI()
	{
		this.cbExportEncoding.setItemLabelGenerator(ee -> ee.charset().name());
		
		this.chbxUseBom.setLabel(this.translate(JasperConfigsLocalization.WITH_BOM));
		
		this.getContent().add(this.cbExportEncoding, this.chbxUseBom);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.cbExportEncoding)
			.asRequired()
			.bind(EncodingConfig::getSelected, EncodingConfig::setSelected);
		
		this.cbExportEncoding.addValueChangeListener(ev -> this.validateAndManageUseBOM(ev.getValue()));
		
		this.binder.forField(this.chbxUseBom)
			.bind(EncodingConfig::isUseBOM, EncodingConfig::setUseBOM);
	}
	
	protected void validateAndManageUseBOM(final ExportEncoding value)
	{
		if(value == null)
		{
			return;
		}
		
		this.chbxUseBom.setEnabled(value.hasBom());
		if(!value.hasBom())
		{
			this.chbxUseBom.setValue(false);
		}
	}
	
	@Override
	public void updateFrom(final EncodingConfig value)
	{
		this.cbExportEncoding.setItems(value.getAvailable());
		
		super.updateFrom(value);
	}
}
