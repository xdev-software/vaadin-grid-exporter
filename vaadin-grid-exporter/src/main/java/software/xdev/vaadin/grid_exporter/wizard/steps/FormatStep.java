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
import java.util.stream.Collectors;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

import software.xdev.vaadin.grid_exporter.GridExportLocalizationConfig;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.wizard.GridExporterWizardState;


public class FormatStep<T> extends AbstractGridExportWizardStepComposite<VerticalLayout, T>
{
	protected final Binder<GridExporterWizardState<T>> binder = new Binder<>();
	
	protected final ComboBox<Format> cbFormats = new ComboBox<>();
	
	protected final VerticalLayout vlConfigs = new VerticalLayout();
	
	protected List<? extends SpecificConfigComponent<? extends SpecificConfig>> configComponents = new ArrayList<>();
	
	public FormatStep(final Translator translator)
	{
		super(translator);
		this.setStepName(this.translate(GridExportLocalizationConfig.FORMAT));
		
		this.initUI();
		
		this.registerListeners();
		
		this.initBindings();
	}
	
	protected void initUI()
	{
		this.cbFormats.setLabel(this.translate(GridExportLocalizationConfig.FORMAT));
		this.cbFormats.setItemLabelGenerator(Format::getFormatNameToDisplay);
		this.cbFormats.setWidthFull();
		this.cbFormats.setMaxWidth("16em");
		
		this.vlConfigs.setSpacing(false);
		this.vlConfigs.setPadding(false);
		this.vlConfigs.setSizeFull();
		
		this.getContent().add(this.cbFormats, this.vlConfigs);
		this.getContent().setPadding(false);
		this.getContent().setSizeFull();
	}
	
	protected void registerListeners()
	{
		this.cbFormats.addValueChangeListener(ev ->
		{
			// Normally this should never happen due to the binder
			final Format newFormat = ev.getValue();
			
			this.showConfigComponentsFor(newFormat);
			
			if(newFormat == null)
			{
				return;
			}
			
			this.bindConfigComponents(this.getWizardState(), true);
		});
	}
	
	protected void showConfigComponentsFor(final Format format)
	{
		this.vlConfigs.removeAll();
		
		this.configComponents = format != null
			? format.getConfigComponents()
				.stream()
				.map(creator -> creator.apply(this))
				.collect(Collectors.toList())
			: Collections.emptyList();
		
		this.configComponents.forEach(c ->
		{
			final Details details = new Details(c.getHeader(), c);
			details.setOpened(true);
			details.addThemeVariants(DetailsVariant.FILLED, DetailsVariant.SMALL);
			details.setWidthFull();
			
			this.vlConfigs.add(details);
		});
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected void bindConfigComponents(final GridExporterWizardState<T> state, final boolean deleteNonMatchingFromState)
	{
		for(final SpecificConfigComponent<?> component : this.configComponents)
		{
			final SpecificConfig newConfig = component.getNewConfigSupplier().get();
			
			final SpecificConfig existing = state.getSpecificConfigs().stream()
				.filter(c -> Objects.equals(newConfig.getClass(), c.getClass()))
				.findFirst()
				.orElse(null);
			
			state.getSpecificConfigs().remove(existing);
			
			final SpecificConfig configToUse = existing == null ? newConfig : existing;
			
			((SpecificConfigComponent)component).updateFrom(configToUse);
			
			state.getSpecificConfigs().add(configToUse);
		}
		
		if(deleteNonMatchingFromState)
		{
			// Remove all configs that are not required for the current state
			state.getSpecificConfigs().removeAll(
				state.getSpecificConfigs()
					.stream()
					.filter(ec -> this.configComponents.stream()
						.map(SpecificConfigComponent::getBean)
						.noneMatch(c -> Objects.equals(ec, c)))
					.collect(Collectors.toList())
			);
		}
	}
	
	protected void initBindings()
	{
		this.binder.forField(this.cbFormats)
			.asRequired()
			.bind(GridExporterWizardState::getSelectedFormat, GridExporterWizardState::setSelectedFormat);
	}
	
	@Override
	public void onEnterStep(final GridExporterWizardState<T> state)
	{
		this.cbFormats.setItems(DataProvider.ofCollection(state.getAvailableFormats()));
		
		this.binder.setBean(state);
		
		this.bindConfigComponents(state, false);
	}
	
	@Override
	public boolean onProgress(final GridExporterWizardState<T> state)
	{
		return this.binder.isValid() && this.configComponents.stream().allMatch(SpecificConfigComponent::isValid);
	}
}
