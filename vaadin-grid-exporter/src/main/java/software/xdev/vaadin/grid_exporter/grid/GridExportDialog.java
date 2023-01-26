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
import java.io.IOException;
import java.util.Objects;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.server.StreamResource;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.format.FormatConfigComponent;
import software.xdev.vaadin.grid_exporter.format.GeneralConfig;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.grid.column.ColumnConfigurationComponent;


/**
 * Dialog where the user can export data from a Vaadin {@link Grid} and configure the format.
 *
 * @author Johannes Rabauer
 * @author AB
 */
@CssImport(GridExporterStyles.LOCATION)
public class GridExportDialog<T> extends Dialog implements AfterNavigationObserver, Translator
{
	private static final int ERROR_NOTIFICATION_DURATION_IN_MS = 3000;
	private Tab tabConfigure;
	private Tab tabPreview;
	private Tabs tabs;
	private ReportViewerComponent viewerComponent;
	private transient final GeneralConfig<T> configuration;
	private transient final GridExportLocalizationConfig localizationConfig;
	private final Grid<T> gridToExport;
	private transient Format<T, ?> selectedFormat;
	private FormatConfigComponent<?> selectedFormatConfigComponent;
	
	public static <T> GridExportDialog<T> open(final Grid<T> gridToExport)
	{
		final GridExportLocalizationConfig gridExportLocalizationConfig = new GridExportLocalizationConfig();
		return open(
			gridToExport,
			new GeneralConfig<>(
				gridToExport,
				(Translator)key -> gridExportLocalizationConfig.getTranslation(key, gridToExport)),
			gridExportLocalizationConfig);
	}
	
	public static <T> GridExportDialog<T> open(final Grid<T> gridToExport, final GeneralConfig<T> configuration)
	{
		return open(gridToExport, configuration, new GridExportLocalizationConfig());
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
		final byte[] exportedGridAsBytes;
		try
		{
			exportedGridAsBytes =
				format.export(this.gridToExport, this.configuration, (E)specificConfigComponent.getConfig());
			
			final StreamResource resource = new StreamResource(
				this.configuration.getFileName() + "." + format.getFormatFilenameSuffix(),
				() -> new ByteArrayInputStream(exportedGridAsBytes));
			resource.setContentType(format.getMimeType());
			
			this.viewerComponent.setData(resource, format.getMimeType());
			this.tabs.setSelectedTab(this.tabPreview);
		}
		catch(final IOException e)
		{
			final Notification errorMessage = new Notification(
				this.translate(GridExportLocalizationConfig.ERROR_EXPORTING),
				ERROR_NOTIFICATION_DURATION_IN_MS,
				Notification.Position.MIDDLE);
			errorMessage.addThemeVariants(NotificationVariant.LUMO_ERROR);
			errorMessage.open();
		}
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
		columnConfigurationComponent.addClassName(GridExporterStyles.PRIMARY_FLEX_CHILD);
		columnConfigurationComponent.addClassName(GridExporterStyles.FLEX_WRAP_CONTAINER);
		columnConfigurationComponent.addClassName(GridExporterStyles.BAR);
		
		// FOOTER
		final Button btnCancel = new Button(this.translate(GridExportLocalizationConfig.CANCEL));
		btnCancel.addClassName(GridExporterStyles.BUTTON);
		final Button btnExport = new Button(this.translate(GridExportLocalizationConfig.EXPORT));
		btnExport.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnExport.addClassName(GridExporterStyles.BUTTON);
		btnExport.addClickListener(event -> this.export(this.selectedFormat, this.selectedFormatConfigComponent));
		btnCancel.addClickListener(event -> this.close());
		
		final HorizontalLayout buttonbar = new HorizontalLayout();
		buttonbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		buttonbar.addClassName(GridExporterStyles.BAR);
		buttonbar.setSpacing(false);
		buttonbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		this.getFooter().add(buttonbar);
		
		this.viewerComponent = new ReportViewerComponent(this);
		this.viewerComponent.setSizeUndefined();
		this.viewerComponent.addClassName(GridExporterStyles.MAIN_LAYOUT);
		final VerticalLayout configurationLayout = new VerticalLayout();
		configurationLayout.setSizeUndefined();
		configurationLayout.addClassName(GridExporterStyles.MAIN_LAYOUT);
		
		// HEADER
		this.tabConfigure = new Tab(
			VaadinIcon.COG.create(),
			new Span(this.translate(GridExportLocalizationConfig.CONFIGURE_COLUMNS)));
		this.tabPreview =
			new Tab(VaadinIcon.VIEWPORT.create(), new Span(this.translate(GridExportLocalizationConfig.PREVIEW)));
		
		this.tabs = new Tabs(this.tabConfigure, this.tabPreview);
		this.tabs.addClassName(GridExporterStyles.BAR);
		this.tabs.addSelectedChangeListener(event -> this.selectedTabChanged(
			btnCancel,
			btnExport,
			buttonbar,
			configurationLayout,
			event)
		);
		
		final Label lblTitle = new Label(this.translate(GridExportLocalizationConfig.EXPORT_CAPTION));
		lblTitle.addClassName(GridExporterStyles.PRIMARY_FLEX_CHILD);
		final Button btnClose = new Button(VaadinIcon.CLOSE.create());
		btnClose.addClickListener(event -> this.close());
		final Icon iconGrid = new Icon(VaadinIcon.TABLE);
		final HorizontalLayout titlebar = new HorizontalLayout();
		titlebar.addClassName(GridExporterStyles.BAR);
		titlebar.addClassName(GridExporterStyles.FLEX_WRAP_CONTAINER);
		titlebar.setFlexGrow(1.0, lblTitle);
		titlebar.add(iconGrid, lblTitle, btnClose);
		titlebar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		this.getHeader().add(new VerticalLayout(titlebar, this.tabs));
		
		final FlexLayout specificConfigurationLayout = new FlexLayout();
		specificConfigurationLayout.addClassName(GridExporterStyles.SPECIFIC_CONFIGURATION_CONTAINER);
		final ComboBox<Format<T, ?>> formatComboBox = new ComboBox<>();
		formatComboBox.setItems(this.configuration.getAvailableFormats());
		formatComboBox.setItemLabelGenerator(item -> item.getFormatNameToDisplay());
		formatComboBox.setLabel(this.translate(GridExportLocalizationConfig.FORMAT));
		formatComboBox.addValueChangeListener(event -> this.selectedFormatChanged(specificConfigurationLayout, event));
		formatComboBox.setValue(this.configuration.getPreselectedFormat());
		
		configurationLayout.setPadding(false);
		configurationLayout.add(columnConfigurationComponent, formatComboBox, specificConfigurationLayout);
		configurationLayout.addClassName(GridExporterStyles.BAR);
		
		this.addClassName(GridExporterStyles.DIALOG);
		// Simply call the "Selection Changed Listener"
		this.tabs.setSelectedTab(this.tabPreview);
		this.tabs.setSelectedTab(this.tabConfigure);
	}
	
	private void selectedTabChanged(
		final Button btnCancel,
		final Button btnExport,
		final HorizontalLayout buttonbar,
		final VerticalLayout configurationLayout,
		final Tabs.SelectedChangeEvent event)
	{
		this.removeAll();
		buttonbar.removeAll();
		if(event.getSelectedTab() == this.tabPreview)
		{
			buttonbar.add(btnCancel, this.viewerComponent.getDownloadAnchor());
			this.add(this.viewerComponent);
		}
		else
		{
			buttonbar.add(btnCancel, btnExport);
			this.add(configurationLayout);
		}
	}
	
	private void selectedFormatChanged(
		final FlexLayout specificConfigurationLayout,
		final AbstractField.ComponentValueChangeEvent<ComboBox<Format<T, ?>>, Format<T, ?>> event)
	{
		specificConfigurationLayout.removeAll();
		this.selectedFormat = event.getValue();
		this.selectedFormatConfigComponent = this.selectedFormat.createConfigurationComponent();
		this.selectedFormatConfigComponent.addClassName(GridExporterStyles.SPECIFIC_CONFIGURATION_CONTAINER);
		specificConfigurationLayout.add(this.selectedFormatConfigComponent);
	}
}
