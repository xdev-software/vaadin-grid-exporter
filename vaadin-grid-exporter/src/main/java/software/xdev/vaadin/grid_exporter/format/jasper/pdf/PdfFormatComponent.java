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
package software.xdev.vaadin.grid_exporter.format.jasper.pdf;

import java.awt.Insets;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;

import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.FormatConfigComponent;
import software.xdev.vaadin.grid_exporter.grid.GridExportLocalizationConfig;


public class PdfFormatComponent
	extends FormatConfigComponent<PdfSpecificConfig>
	implements Translator
{
	private final Translator translator;
	private ComboBox<PageOrientation> cmbPageOrientation;
	private ComboBox<PageType> cmbPageFormat;
	private Checkbox ckShowPageNumbers, ckHighlightRows;
	private final TextField txtReportTitle = new TextField();
	
	PdfFormatComponent(final Translator translator)
	{
		this.translator = translator;
		this.initUI();
	}
	
	private void initUI()
	{
		this.ckShowPageNumbers = new Checkbox();
		this.ckHighlightRows = new Checkbox();
		this.cmbPageFormat = new ComboBox<>();
		this.cmbPageOrientation = new ComboBox<>();
		this.cmbPageFormat.setLabel(this.translate(GridExportLocalizationConfig.PAGE_SIZE));
		this.cmbPageOrientation.setLabel(this.translate(GridExportLocalizationConfig.PAGE_ORIENTATION));
		this.ckShowPageNumbers.setLabel(this.translate(GridExportLocalizationConfig.SHOW_PAGE_NUMBERS));
		this.ckHighlightRows.setLabel(this.translate(GridExportLocalizationConfig.HIGHLIGHT_ROWS));
		
		this.txtReportTitle.setLabel(this.translate(GridExportLocalizationConfig.REPORT_TITLE));
		
		this.cmbPageOrientation.setItems(PageOrientation.values());
		this.cmbPageOrientation.setItemLabelGenerator(orientation ->
			orientation == PageOrientation.PORTRAIT
				? this.translate(GridExportLocalizationConfig.PORTRAIT)
				: this.translate(GridExportLocalizationConfig.LANDSCAPE));
		this.cmbPageOrientation.setValue(PageOrientation.PORTRAIT);
		
		this.cmbPageFormat.setItems(PageType.values());
		this.cmbPageFormat.setValue(PageType.A4);
		
		this.add(
			this.txtReportTitle,
			this.cmbPageFormat,
			this.cmbPageOrientation,
			this.ckShowPageNumbers,
			this.ckHighlightRows);
	}
	
	@Override
	public String translate(final String key)
	{
		return this.translator != null ? this.translator.translate(key) : key;
	}
	
	@Override
	public PdfSpecificConfig getConfig()
	{
		return
			new PdfSpecificConfig()
				.withPageType(this.cmbPageFormat.getValue())
				.withHighlightRows(this.ckHighlightRows.getValue())
				.withPageOrientation(this.cmbPageOrientation.getValue())
				.withShowPageNumber(this.ckShowPageNumbers.getValue())
				.withPageMargin(new Insets(20, 20, 20, 20))
				.withTitle(this.txtReportTitle.getValue())
			;
	}
}
