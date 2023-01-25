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
package software.xdev.vaadin.grid_exporter.grid.column;

import java.util.Collections;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.grid.GridExportLocalizationConfig;
import software.xdev.vaadin.grid_exporter.grid.GridExporterStyles;


public class ColumnConfigurationComponent<T> extends FlexLayout implements Translator
{
	private final Grid<ColumnConfiguration<T>> grid = new Grid<>();
	private final List<ColumnConfiguration<T>> columnConfigurations;
	private final Translator translator;
	
	public ColumnConfigurationComponent(
		final Translator translator,
		final List<ColumnConfiguration<T>> columnConfigurations)
	{
		this.translator = translator;
		this.columnConfigurations = columnConfigurations;
		this.initUI();
	}
	
	@Override
	public String translate(final String key)
	{
		return this.translator.translate(key);
	}
	
	public void initUI()
	{
		this.grid.setItems(this.columnConfigurations);
		this.grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
		
		this.grid.addColumn(new ComponentRenderer<>(v ->
			{
				final TextField txtField = new TextField();
				txtField.setRequired(true);
				txtField.setWidthFull();
				
				txtField.setValue(v.getHeader());
				txtField.addValueChangeListener(e -> v.setHeader(e.getValue()));
				txtField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
				
				return txtField;
			}))
			.setKey("name")
			.setHeader(this.translate(GridExportLocalizationConfig.NAME))
			.setResizable(true)
			.setSortable(false)
			.setAutoWidth(true);
		
		this.grid
			.addColumn(new ComponentRenderer<>(v ->
			{
				final Button up = new Button(
					VaadinIcon.ARROW_UP.create(),
					e -> this.moveUp(v));
				up.addThemeVariants(ButtonVariant.LUMO_SMALL);
				final Button down = new Button(
					VaadinIcon.ARROW_DOWN.create(),
					e -> this.moveDown(v));
				down.addThemeVariants(ButtonVariant.LUMO_SMALL);
				
				final HorizontalLayout hlContainer = new HorizontalLayout(up, down);
				hlContainer.setSizeFull();
				hlContainer.setMargin(false);
				hlContainer.setPadding(false);
				return hlContainer;
			}))
			.setKey("position")
			.setHeader(this.translate(GridExportLocalizationConfig.POSITION))
			.setSortable(false)
			.setWidth("110px")
			.setFlexGrow(0);
		
		this.grid.setSelectionMode(Grid.SelectionMode.MULTI);
		((GridMultiSelectionModel<?>)this.grid.getSelectionModel())
			.setSelectAllCheckboxVisibility(GridMultiSelectionModel.SelectAllCheckboxVisibility.VISIBLE);
		this.grid.addSelectionListener(event ->
		{
			this.columnConfigurations.forEach(column -> column.setVisible(false));
			this.grid.getSelectedItems().forEach(column -> column.setVisible(true));
		});
		this.columnConfigurations.forEach(column -> this.grid.select(column));
		this.addClassName(GridExporterStyles.COLUMN_GRID);
		this.add(this.grid);
	}
	
	private void moveUp(final ColumnConfiguration<T> column)
	{
		final List<ColumnConfiguration<T>> columns = this.columnConfigurations;
		final int index = columns.indexOf(column);
		if(index > 0 && columns.size() > 1)
		{
			Collections.swap(columns, index - 1, index);
		}
		this.grid.getDataProvider().refreshAll();
	}
	
	private void moveDown(final ColumnConfiguration<T> column)
	{
		final List<ColumnConfiguration<T>> columns = this.columnConfigurations;
		final int index = columns.indexOf(column);
		if(index < columns.size() - 1 && columns.size() > 1)
		{
			Collections.swap(columns, index, index + 1);
		}
		this.grid.getDataProvider().refreshAll();
	}
}
