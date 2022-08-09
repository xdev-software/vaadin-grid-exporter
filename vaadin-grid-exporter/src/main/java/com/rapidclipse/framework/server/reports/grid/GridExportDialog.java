package com.rapidclipse.framework.server.reports.grid;

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

import static java.util.Objects.requireNonNull;

import java.awt.Insets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.rapidclipse.framework.server.reports.Format;
import com.rapidclipse.framework.server.reports.grid.column.ColumnConfiguration;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.server.StreamResource;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;


public class GridExportDialog<T> extends Dialog implements AfterNavigationObserver
{
	public static <T> GridExportDialog<T> open(final Grid<T> gridToExport)
	{
		return open(new GridExportConfiguration<>(gridToExport));
	}
	
	public static <T> GridExportDialog<T> open(final GridExportConfiguration<T> configuration)
	{
		return open(configuration, new GridExportLocalizationConfig());
	}
	
	public static <T> GridExportDialog<T> open(
		final Grid<T> gridToExport,
		final GridExportLocalizationConfig localizationConfig)
	{
		return open(new GridExportConfiguration<>(gridToExport), localizationConfig);
	}
	
	public static <T> GridExportDialog<T> open(
		final GridExportConfiguration<T> configuration,
		final GridExportLocalizationConfig localizationConfig)
	{
		final GridExportDialog<T> dialog = new GridExportDialog<>(configuration, localizationConfig);
		dialog.open();
		return dialog;
	}
	
	private final GridExportConfiguration<T> configuration;
	private final GridExportLocalizationConfig localizationConfig;
	private GridReportBuilder<T> gridReportBuilder = GridReportBuilder.New();
	
	
	public GridExportDialog(
		final GridExportConfiguration<T> configuration,
		final GridExportLocalizationConfig localizationConfig)
	{
		super();
		
		this.configuration = configuration;
		this.localizationConfig = Objects.requireNonNull(localizationConfig);
		
		this.initUI();
		
		this.txtReportTitle.setValue(configuration.getTitle());
		
		final Format[] formats = configuration.getAvailableFormats();
		Arrays.sort(formats, Comparator.comparing(Format::name));
		
		this.cmbExportFormat.setItems(formats);
		if(formats.length > 0)
		{
			final Format presetFormat = configuration.getFormat();
			this.cmbExportFormat.setValue(presetFormat != null ? presetFormat : formats[0]);
		}
		this.cmbExportFormat.setItemLabelGenerator(Format::name);
		
		this.cmbPageOrientation.setItems(PageOrientation.values());
		this.cmbPageOrientation.setValue(configuration.getPageOrientation());
		this.cmbPageOrientation.setItemLabelGenerator(orientation ->
			orientation == PageOrientation.PORTRAIT
				? this.translate(GridExportLocalizationConfig.PORTRAIT)
				: this.translate(GridExportLocalizationConfig.LANDSCAPE));
		
		this.cmbPageFormat.setItems(PageType.values());
		this.cmbPageFormat.setValue(configuration.getPageType());
		
		this.grid.getColumnByKey("visible").setHeader(new Icon(VaadinIcon.EYE));
		this.grid.setItems(configuration.getColumnConfigurations());
		this.grid.recalculateColumnWidths();
		
		this.ckShowPageNumbers.setValue(configuration.isShowPageNumber());
		this.ckHighlightRows.setValue(configuration.isHighlightRows());
		
		final Page page = UI.getCurrent().getPage();
		page.retrieveExtendedClientDetails(e -> this.adjustGridColumns(e.getBodyClientWidth()));
		page.addBrowserWindowResizeListener(e -> this.adjustGridColumns(e.getWidth()));
	}
	
	private String translate(final String key)
	{
		return this.localizationConfig.getTranslation(key, this);
	}
	
	public void setGridReportBuilder(final GridReportBuilder<T> gridReportBuilder)
	{
		this.gridReportBuilder = requireNonNull(gridReportBuilder);
	}
	
	public GridReportBuilder<T> getGridReportBuilder()
	{
		return this.gridReportBuilder;
	}
	
	private void adjustGridColumns(final int width)
	{
		final boolean visible = width >= 666;
		this.grid.getColumnByKey("width").setVisible(visible);
		this.grid.getColumnByKey("position").setVisible(visible);
	}
	
	private void moveUp(final ColumnConfiguration<T> column)
	{
		final List<ColumnConfiguration<T>> columns = this.configuration.getColumnConfigurations();
		final int index = columns.indexOf(column);
		if(index > 0 && columns.size() > 1)
		{
			Collections.swap(columns, index - 1, index);
		}
		this.grid.getDataProvider().refreshAll();
	}
	
	private void moveDown(final ColumnConfiguration<T> column)
	{
		final List<ColumnConfiguration<T>> columns = this.configuration.getColumnConfigurations();
		final int index = columns.indexOf(column);
		if(index < columns.size() - 1 && columns.size() > 1)
		{
			Collections.swap(columns, index, index + 1);
		}
		this.grid.getDataProvider().refreshAll();
	}
	
	private boolean check()
	{
		boolean ok = false;
		String error = "";
		
		if(this.checkColumnSelection())
		{
			if(this.checkColumnWidth())
			{
				if(this.cmbExportFormat.getValue() != null)
				{
					ok = true;
				}
			}
			else
			{
				error = this.translate(GridExportLocalizationConfig.WIDTH_TO_BIG_ERROR);
			}
		}
		
		this.btnExport.setEnabled(ok);
		this.lblStatus.setText(error);
		
		return ok;
	}
	
	private boolean checkColumnWidth()
	{
		if(this.cmbPageOrientation.getValue() != null && this.cmbPageFormat.getValue() != null)
		{
			final Insets margin = this.configuration.getPageMargin();
			if(this.cmbPageOrientation.getValue().equals(PageOrientation.PORTRAIT))
			{
				return this.calculateFixedWidth() <= this.cmbPageFormat.getValue()
					.getWidth() - margin.left - margin.right;
			}
			else
			{
				return this.calculateFixedWidth() <= this.cmbPageFormat.getValue()
					.getHeight() - margin.left - margin.right;
			}
		}
		
		return true;
	}
	
	private int calculateFixedWidth()
	{
		return this.configuration.getColumnConfigurations().stream()
			.filter(ColumnConfiguration::isVisible)
			.map(ColumnConfiguration::getColumnWidth)
			.filter(Objects::nonNull)
			.mapToInt(Integer::intValue)
			.sum();
	}
	
	private boolean checkColumnSelection()
	{
		return this.configuration.getColumnConfigurations().stream()
			.anyMatch(ColumnConfiguration::isVisible);
	}
	
	private StreamResource createReportResource()
	{
		final Format format = this.cmbExportFormat.getValue();
		this.configuration.setFormat(format);
		this.configuration.setHighlightRows(this.ckHighlightRows.getValue());
		this.configuration.setPageOrientation(this.cmbPageOrientation.getValue());
		this.configuration.setPageType(this.cmbPageFormat.getValue());
		this.configuration.setShowPageNumber(this.ckShowPageNumbers.getValue());
		this.configuration.setTitle(this.txtReportTitle.getValue());
		
		final JasperReportBuilder builder = this.gridReportBuilder.buildReport(this.configuration);
		return format.createExporter().exportToResource(builder, this.configuration.getTitle());
	}
	
	private void setAllColumnsVisible(final boolean visible)
	{
		this.configuration.getColumnConfigurations().forEach(conf -> conf.setVisible(visible));
		this.grid.getDataProvider().refreshAll();
		this.check();
	}
	
	private void export()
	{
		if(!this.check())
		{
			return;
		}
		
		final StreamResource res = this.createReportResource();
		new ReportViewerDialog(res, this.cmbExportFormat.getValue(), this.localizationConfig).open();
	}
	
	@Override
	public void afterNavigation(final AfterNavigationEvent event)
	{
		// Workaround for https://github.com/vaadin/vaadin-dialog-flow/issues/108
		this.close();
	}
	
	private void initUI()
	{
		this.layout = new VerticalLayout();
		this.titlebar = new HorizontalLayout();
		this.iconGrid = new Icon(VaadinIcon.TABLE);
		this.lblTitle = new Label();
		this.txtReportTitle = new TextField();
		this.lblGridTitle = new Label();
		this.gridcontent = new VerticalLayout();
		this.gridcontainer = new FlexLayout();
		this.grid = new Grid<>();
		this.gridselectors = new VerticalLayout();
		this.btnSelectAll = new Button();
		this.btnClearSelection = new Button();
		this.optionscontainer = new HorizontalLayout();
		this.cmbPageFormat = new ComboBox<>();
		this.cmbPageOrientation = new ComboBox<>();
		this.configurationcheckboxes = new VerticalLayout();
		this.ckShowPageNumbers = new Checkbox();
		this.ckHighlightRows = new Checkbox();
		this.cmbExportFormat = new ComboBox<>();
		this.bottomlayout = new HorizontalLayout();
		this.lblStatus = new Label();
		this.buttonbar = new HorizontalLayout();
		this.btnCancel = new Button();
		this.btnExport = new Button();
		
		this.layout.setSpacing(false);
		this.layout.setMaxHeight("100%");
		this.layout.setMaxWidth("100%");
		this.layout.setPadding(false);
		this.lblTitle.setText(this.translate(GridExportLocalizationConfig.EXPORT_CAPTION));
		this.txtReportTitle.setMaxWidth("100%");
		this.txtReportTitle.setLabel(this.translate(GridExportLocalizationConfig.REPORT_TITLE));
		this.lblGridTitle.setText(this.translate(GridExportLocalizationConfig.CONFIGURE_COLUMNS));
		this.lblGridTitle.getStyle().set("padding-top", "10px");
		this.gridcontainer.setFlexWrap(FlexWrap.WRAP);
		this.grid.setMaxWidth("100%");
		this.grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
		this.grid
			.addColumn(new ComponentRenderer<>(v ->
			{
				final Checkbox chbx = new Checkbox(v.isVisible());
				chbx.addValueChangeListener(e ->
				{
					v.setVisible(e.getValue());
					this.check();
				});
				return chbx;
			}))
			.setKey("visible")
			.setHeader(" ")
			.setResizable(true)
			.setSortable(false)
			.setWidth("60px")
			.setFlexGrow(0);
		
		this.grid.addColumn(new ComponentRenderer<>(v ->
			{
				final TextField txtField = new TextField();
				txtField.setRequired(true);
				txtField.setWidthFull();
				
				txtField.setValue(v.getHeader());
				txtField.addValueChangeListener(e -> v.setHeader(e.getValue()));
				
				return txtField;
			}))
			.setKey("name")
			.setHeader(this.translate(GridExportLocalizationConfig.NAME))
			.setResizable(true)
			.setSortable(false)
			.setWidth("350px")
			.setAutoWidth(true);
		
		this.grid.addColumn(new ComponentRenderer<>(v ->
			{
				final IntegerField ifField = new IntegerField();
				ifField.setWidthFull();
				
				ifField.setValue(v.getColumnWidth());
				ifField.addValueChangeListener(e ->
				{
					v.setColumnWidth(e.getValue());
					this.check();
				});
				
				return ifField;
			}))
			.setKey("width")
			.setHeader(this.translate(GridExportLocalizationConfig.WIDTH))
			.setResizable(true)
			.setSortable(false)
			.setWidth("100px")
			.setFlexGrow(0);
		
		this.grid
			.addColumn(new ComponentRenderer<>(v ->
			{
				final Button up = new Button(
					VaadinIcon.ARROW_UP.create(),
					e -> this.moveUp(v));
				final Button down = new Button(
					VaadinIcon.ARROW_DOWN.create(),
					e -> this.moveDown(v));
				
				final HorizontalLayout hlContainer = new HorizontalLayout(up, down);
				hlContainer.setSizeFull();
				hlContainer.setMargin(false);
				hlContainer.setPadding(false);
				return hlContainer;
			}))
			.setKey("position")
			.setHeader(this.translate(GridExportLocalizationConfig.POSITION))
			.setSortable(false)
			.setWidth("150px")
			.setFlexGrow(0);
		
		this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		this.gridselectors.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
		this.btnSelectAll.setText(this.translate(GridExportLocalizationConfig.SELECT_ALL));
		this.btnClearSelection.setText(this.translate(GridExportLocalizationConfig.SELECT_NONE));
		this.optionscontainer.setPadding(false);
		this.optionscontainer.setWidthFull();
		this.cmbPageFormat.setLabel(this.translate(GridExportLocalizationConfig.PAGE_SIZE));
		this.cmbPageOrientation.setLabel(this.translate(GridExportLocalizationConfig.PAGE_ORIENTATION));
		this.ckShowPageNumbers.setLabel(this.translate(GridExportLocalizationConfig.SHOW_PAGE_NUMBERS));
		this.ckHighlightRows.setLabel(this.translate(GridExportLocalizationConfig.HIGHLIGHT_ROWS));
		this.cmbExportFormat.setLabel(this.translate(GridExportLocalizationConfig.FORMAT));
		this.bottomlayout.setSpacing(false);
		this.bottomlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		this.lblStatus.getStyle().set("color", "red");
		this.buttonbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		this.btnCancel.setText(this.translate(GridExportLocalizationConfig.CANCEL));
		this.btnExport.setText(this.translate(GridExportLocalizationConfig.EXPORT));
		this.btnExport.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		this.btnClose = new Button(VaadinIcon.CLOSE.create());
		
		this.titlebar.add(this.iconGrid, this.lblTitle, this.btnClose);
		this.titlebar.setWidthFull();
		this.titlebar.setHeight(null);
		this.titlebar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		this.titlebar.setFlexGrow(1.0, this.lblTitle);
		
		this.btnSelectAll.setWidthFull();
		this.btnSelectAll.setHeight(null);
		this.btnClearSelection.setWidthFull();
		this.btnClearSelection.setHeight(null);
		this.gridselectors.add(this.btnSelectAll, this.btnClearSelection);
		this.grid.setWidth("600px");
		this.grid.setHeight("400px");
		this.gridselectors.setSizeUndefined();
		this.gridcontainer.add(this.grid, this.gridselectors);
		this.gridcontainer.setFlexGrow(1.0, this.grid);
		this.ckShowPageNumbers.setSizeUndefined();
		this.ckHighlightRows.setSizeUndefined();
		this.configurationcheckboxes.setPadding(false);
		this.configurationcheckboxes.add(this.ckShowPageNumbers, this.ckHighlightRows);
		this.cmbPageFormat.setSizeFull();
		this.cmbPageOrientation.setSizeFull();
		this.configurationcheckboxes.setSizeFull();
		this.cmbExportFormat.setSizeFull();
		this.optionscontainer.add(this.configurationcheckboxes, this.cmbPageFormat, this.cmbPageOrientation,
			this.cmbExportFormat);
		this.gridcontainer.setWidthFull();
		this.gridcontainer.setHeight(null);
		this.optionscontainer.setSizeUndefined();
		this.gridcontent.setPadding(false);
		this.gridcontent.add(this.gridcontainer, this.optionscontainer);
		this.btnCancel.setSizeUndefined();
		this.btnExport.setSizeUndefined();
		this.buttonbar.add(this.btnCancel, this.btnExport);
		this.lblStatus.setWidthFull();
		this.lblStatus.setHeight(null);
		this.buttonbar.setWidthFull();
		this.buttonbar.setHeight(null);
		this.bottomlayout.add(this.lblStatus, this.buttonbar);
		this.txtReportTitle.setWidth("400px");
		this.txtReportTitle.setHeight(null);
		this.lblGridTitle.setSizeUndefined();
		this.gridcontent.setWidthFull();
		this.gridcontent.setHeight(null);
		this.bottomlayout.setWidthFull();
		this.bottomlayout.setHeight(null);
		this.layout.add(this.titlebar, this.txtReportTitle, this.lblGridTitle, this.gridcontent, this.bottomlayout);
		this.layout.setWidth("1000px");
		this.layout.setHeight(null);
		this.add(this.layout);
		this.setSizeUndefined();
		
		this.txtReportTitle.addValueChangeListener(event -> this.check());
		this.btnSelectAll.addClickListener(event -> this.setAllColumnsVisible(true));
		this.btnClearSelection.addClickListener(event -> this.setAllColumnsVisible(false));
		this.cmbPageFormat.addValueChangeListener(event -> this.check());
		this.cmbPageOrientation.addValueChangeListener(event -> this.check());
		this.cmbExportFormat.addValueChangeListener(event -> this.check());
		this.btnCancel.addClickListener(event -> this.close());
		this.btnClose.addClickListener(event -> this.close());
		this.btnExport.addClickListener(event -> this.export());
	}
	
	private Checkbox ckShowPageNumbers, ckHighlightRows;
	private Button btnSelectAll, btnClearSelection, btnCancel, btnExport;
	private FlexLayout gridcontainer;
	private HorizontalLayout optionscontainer;
	private Grid<ColumnConfiguration<T>> grid;
	private VerticalLayout layout, gridcontent, configurationcheckboxes, gridselectors;
	private HorizontalLayout titlebar, bottomlayout, buttonbar;
	private Label lblTitle, lblGridTitle, lblStatus;
	private Button btnClose;
	private ComboBox<PageType> cmbPageFormat;
	private ComboBox<Format> cmbExportFormat;
	private Icon iconGrid;
	private TextField txtReportTitle;
	private ComboBox<PageOrientation> cmbPageOrientation;
}
