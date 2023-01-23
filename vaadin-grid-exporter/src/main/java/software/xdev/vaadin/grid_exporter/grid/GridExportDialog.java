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
package software.xdev.vaadin.grid_exporter.grid;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.server.StreamResource;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.format.FormatConfigComponent;
import software.xdev.vaadin.grid_exporter.format.GeneralConfig;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.grid.column.ColumnConfigurationComponent;


@CssImport(GridExporterStyles.LOCATION)
public class GridExportDialog<T> extends Dialog implements AfterNavigationObserver, Translator
{
	private final GeneralConfig<T> configuration;
	private final GridExportLocalizationConfig localizationConfig;
	private final Grid<T> gridToExport;
	
	public static <T> GridExportDialog<T> open(final Grid<T> gridToExport)
	{
		final GridExportLocalizationConfig gridExportLocalizationConfig = new GridExportLocalizationConfig();
		return open(
			gridToExport,
			new GeneralConfig(
				gridToExport,
				(Translator)key -> gridExportLocalizationConfig.getTranslation(key, gridToExport)),
			gridExportLocalizationConfig);
	}
	
	public static <T> GridExportDialog<T> open(final Grid<T> gridToExport, final GeneralConfig<T> configuration)
	{
		return open(gridToExport, configuration);
	}
	
	public static <T> GridExportDialog<T> open(
		final Grid<T> gridToExport,
		final GridExportLocalizationConfig localizationConfig)
	{
		return open(gridToExport, new GeneralConfig<>(gridToExport), localizationConfig);
	}
	
	public static <T> GridExportDialog<T> open(
		final Grid<T> gridToExport,
		final GeneralConfig<T> configuration,
		final GridExportLocalizationConfig localizationConfig)
	{
		final GridExportDialog<T> dialog = new GridExportDialog<>(gridToExport, configuration, localizationConfig);
		dialog.open();
		return dialog;
	}
	
	public GridExportDialog(
		final Grid<T> gridToExport,
		final GeneralConfig<T> configuration,
		final GridExportLocalizationConfig localizationConfig)
	{
		super();
		this.gridToExport = gridToExport;
		this.configuration = configuration;
		this.localizationConfig = Objects.requireNonNull(localizationConfig);
		
		this.initUI();
		
	}
	
	@Override
	public String translate(final String key)
	{
		return this.localizationConfig.getTranslation(key, this);
	}
	
	private <E extends SpecificConfig> void export(
		final Format<T, E> format,
		final FormatConfigComponent<?> specificConfigComponent)
	{
		final byte[] exportedGridAsBytes =
			format.export(this.gridToExport, this.configuration, (E)specificConfigComponent.getConfig());
		
		final StreamResource resource = new StreamResource(
			this.configuration.getFileName() + "." + format.getFormatFilenameSuffix(),
			() -> new ByteArrayInputStream(exportedGridAsBytes));
		resource.setContentType(format.getMimeType());
		
		new ReportViewerDialog(resource, format, this).open();
	}
	
	@Override
	public void afterNavigation(final AfterNavigationEvent event)
	{
		// Workaround for https://github.com/vaadin/vaadin-dialog-flow/issues/108
		this.close();
	}
	
	private void initUI()
	{
		final ColumnConfigurationComponent<T> columnConfigurationComponent =
			new ColumnConfigurationComponent<>(this, this.configuration.getColumnConfigurations());
		
		final Label lblTitle = new Label(this.translate(GridExportLocalizationConfig.EXPORT_CAPTION));
		final Button btnClose = new Button(VaadinIcon.CLOSE.create());
		final Icon iconGrid = new Icon(VaadinIcon.TABLE);
		final HorizontalLayout titlebar = new HorizontalLayout();
		titlebar.add(iconGrid, lblTitle, btnClose);
		titlebar.setWidthFull();
		titlebar.setHeight(null);
		titlebar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		titlebar.setFlexGrow(1.0, lblTitle);
		
		final Button btnCancel = new Button(this.translate(GridExportLocalizationConfig.CANCEL));
		final Button btnExport = new Button(this.translate(GridExportLocalizationConfig.EXPORT));
		btnExport.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnCancel.setSizeUndefined();
		btnExport.setSizeUndefined();
		btnCancel.addClickListener(event -> this.close());
		btnClose.addClickListener(event -> this.close());
		
		final HorizontalLayout buttonbar = new HorizontalLayout();
		buttonbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		buttonbar.add(btnCancel, btnExport);
		buttonbar.setWidthFull();
		buttonbar.setHeight(null);
		
		final Label lblStatus = new Label();
		lblStatus.getStyle().set("color", "red");
		lblStatus.setWidthFull();
		lblStatus.setHeight(null);
		
		final HorizontalLayout bottomlayout = new HorizontalLayout();
		bottomlayout.setSpacing(false);
		bottomlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		bottomlayout.add(lblStatus, buttonbar);
		bottomlayout.setWidthFull();
		bottomlayout.setHeight(null);
		
		columnConfigurationComponent.setFlexWrap(FlexWrap.WRAP);
		columnConfigurationComponent.setFlexGrow(1.0, columnConfigurationComponent);
		columnConfigurationComponent.setWidthFull();
		columnConfigurationComponent.setHeight(null);
		
		final VerticalLayout gridcontent = new VerticalLayout();
		gridcontent.setPadding(false);
		final HorizontalLayout specificConfigurationLayout = new HorizontalLayout();
		final ComboBox<Format<T, ?>> formatComboBox = new ComboBox<>();
		gridcontent.add(columnConfigurationComponent, formatComboBox, specificConfigurationLayout);
		gridcontent.setWidthFull();
		gridcontent.setHeight(null);
		
		formatComboBox.setItems(this.configuration.getAvailableFormats());
		formatComboBox.setItemLabelGenerator(item -> item.getFormatNameToDisplay());
		formatComboBox.setLabel(this.translate(GridExportLocalizationConfig.FORMAT));
		formatComboBox.addValueChangeListener(
			event -> {
				specificConfigurationLayout.removeAll();
				final Format<T, ? extends SpecificConfig> value = event.getValue();
				final FormatConfigComponent<?> configurationComponent = value.getConfigurationComponent();
				specificConfigurationLayout.add(configurationComponent);
				
				btnExport.addClickListener(e -> this.export(value, configurationComponent));
			}
		);
		formatComboBox.setValue(this.configuration.getPreselectedFormat());
		
		final Label lblGridTitle = new Label(this.translate(GridExportLocalizationConfig.CONFIGURE_COLUMNS));
		lblGridTitle.getStyle().set("padding-top", "10px");
		lblGridTitle.setSizeUndefined();
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(false);
		layout.setMaxHeight("100%");
		layout.setMaxWidth("100%");
		layout.setPadding(false);
		layout.add(titlebar, lblGridTitle, gridcontent, bottomlayout);
		layout.setWidth("1000px");
		layout.setHeight(null);
		this.add(layout);
		this.setSizeUndefined();
	}
}
