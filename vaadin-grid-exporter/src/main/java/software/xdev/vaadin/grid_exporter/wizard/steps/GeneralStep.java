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
package software.xdev.vaadin.grid_exporter.wizard.steps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;

import software.xdev.vaadin.grid_exporter.GridExportLocalizationConfig;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.wizard.GridExporterWizardState;


public class GeneralStep<T> extends AbstractGridExportWizardStepComposite<FormLayout, T>
{
	protected final Binder<GridExporterWizardState<T>> binder = new Binder<>();
	
	protected final TextField txtFileName = new TextField();
	protected final Grid<ColumnConfiguration<T>> gridColumns = new Grid<>();
	protected Registration gridSelectionChanged = null;
	
	public GeneralStep(final Translator translator)
	{
		super(translator);
		this.setStepName(this.translate(GridExportLocalizationConfig.GENERAL));
		
		this.initUI();
		
		this.initBindings();
	}
	
	protected void initUI()
	{
		// Common not allowed characters
		this.txtFileName.setAllowedCharPattern("[^<>:\"|?* \\\\\\/\\.]");
		this.txtFileName.setWidthFull();
		
		this.gridColumns.addColumn(new ComponentRenderer<>(v ->
			{
				final TextField txtField = new TextField();
				txtField.setRequired(true);
				txtField.setWidthFull();
				
				txtField.setValue(v.getHeader());
				txtField.addValueChangeListener(e -> {
					v.setHeader(Objects.requireNonNullElse(e.getValue(), ""));
					this.validateGrid();
				});
				txtField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
				
				return txtField;
			}))
			.setHeader(this.translate(GridExportLocalizationConfig.NAME))
			.setResizable(true)
			.setSortable(false)
			.setAutoWidth(true)
			.setFlexGrow(1);
		
		this.gridColumns
			.addColumn(new ComponentRenderer<>(v ->
			{
				final Button up = new Button(VaadinIcon.ARROW_UP.create(), e -> this.move(false, v));
				up.setEnabled(this.isMovingPossible(false, v));
				
				final Button down = new Button(VaadinIcon.ARROW_DOWN.create(), e -> this.move(true, v));
				down.setEnabled(this.isMovingPossible(true, v));
				
				Stream.of(up, down).forEach(btn -> btn.addThemeVariants(ButtonVariant.LUMO_SMALL));
				
				final HorizontalLayout hlContainer = new HorizontalLayout(up, down);
				hlContainer.setSizeFull();
				hlContainer.setMargin(false);
				hlContainer.setPadding(false);
				return hlContainer;
			}))
			.setHeader(this.translate(GridExportLocalizationConfig.POSITION))
			.setSortable(false)
			.setAutoWidth(true)
			.setFlexGrow(0);
		
		this.gridColumns.addThemeVariants(GridVariant.LUMO_COMPACT);
		this.gridColumns.setAllRowsVisible(true);
		this.gridColumns.setSelectionMode(Grid.SelectionMode.MULTI);
		
		this.getContent().setResponsiveSteps(
			new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));
		this.getContent().addFormItem(this.txtFileName, this.translate(GridExportLocalizationConfig.FILENAME));
		this.getContent().addFormItem(this.gridColumns, this.translate(GridExportLocalizationConfig.COLUMNS));
		this.getContent().setSizeFull();
	}
	
	protected void initBindings()
	{
		this.binder.forField(this.txtFileName)
			.asRequired()
			.bind(GridExporterWizardState::getFileName, GridExporterWizardState::setFileName);
	}
	
	@Override
	public void onEnterStep(final GridExporterWizardState<T> state)
	{
		this.binder.setBean(state);
		
		// Unbind listener so that we don't do useless updates
		if(this.gridSelectionChanged != null)
		{
			this.gridSelectionChanged.remove();
		}
		
		this.gridColumns.setItems(DataProvider.ofCollection(state.getAvailableColumns()));
		state.getSelectedColumns().forEach(this.gridColumns::select);
		
		// Rebind listener
		// Can't bind a grid - doing that manually
		this.gridSelectionChanged = this.gridColumns.addSelectionListener(ev ->
		{
			this.getWizardState().setSelectedColumns(new ArrayList<>(ev.getAllSelectedItems()));
			this.validateGrid();
		});
	}
	
	protected void validateGrid()
	{
		this.gridColumns.getStyle().set(
			"border",
			this.isColumnsInvalid(this.getWizardState()) ? "1px solid var(--lumo-error-color-50pct)" : null);
	}
	
	@Override
	public boolean onProgress(final GridExporterWizardState<T> state)
	{
		if(this.isColumnsInvalid(state))
		{
			return false;
		}
		
		return this.binder.isValid();
	}
	
	protected boolean isColumnsInvalid(final GridExporterWizardState<T> state)
	{
		return state.getSelectedColumns().isEmpty()
			|| state.getSelectedColumns()
			.stream()
			.map(ColumnConfiguration::getHeader)
			.anyMatch(String::isBlank);
	}
	
	protected boolean isMovingPossible(final boolean increment, final ColumnConfiguration<T> column)
	{
		final List<ColumnConfiguration<T>> columns = this.getWizardState().getAvailableColumns();
		if(columns.size() <= 1)
		{
			return false;
		}
		
		final int index = columns.indexOf(column);
		return ((increment && index < columns.size() - 1)
			|| (!increment && index > 0));
	}
	
	protected void move(final boolean increment, final ColumnConfiguration<T> column)
	{
		if(!this.isMovingPossible(increment, column))
		{
			return;
		}
		
		final List<ColumnConfiguration<T>> columns = this.getWizardState().getAvailableColumns();
		final int index = columns.indexOf(column);
		Collections.swap(columns, index - (increment ? 0 : 1), index + (increment ? 1 : 0));
		
		this.gridColumns.getDataProvider().refreshAll();
	}
}
