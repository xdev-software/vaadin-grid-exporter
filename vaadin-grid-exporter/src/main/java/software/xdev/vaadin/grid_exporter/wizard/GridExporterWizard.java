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
package software.xdev.vaadin.grid_exporter.wizard;

import java.util.Objects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

import software.xdev.vaadin.grid_exporter.GridExportLocalizationConfig;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.components.wizard.buttonbar.WizardButtonBarWithAnchor;
import software.xdev.vaadin.grid_exporter.components.wizard.panel.WizardPanel;
import software.xdev.vaadin.grid_exporter.wizard.steps.FormatStep;
import software.xdev.vaadin.grid_exporter.wizard.steps.GeneralStep;
import software.xdev.vaadin.grid_exporter.wizard.steps.PreviewStep;


@SuppressWarnings("java:S1948")
public class GridExporterWizard<T> extends Dialog implements AfterNavigationObserver, Translator
{
	protected final Button closeButton = new Button(VaadinIcon.CLOSE.create());
	protected final WizardPanel<GridExporterWizardState<T>> wizardPanel = new WizardPanel<>();
	protected final WizardButtonBarWithAnchor buttonBar = new WizardButtonBarWithAnchor(this.wizardPanel);
	protected final PreviewStep<T> previewStep;
	
	protected final GridExportLocalizationConfig localizationConfig;
	
	public GridExporterWizard(
		final GridExporterWizardState<T> initialState,
		final GridExportLocalizationConfig localizationConfig)
	{
		this.localizationConfig = Objects.requireNonNull(localizationConfig);
		// Needs to be done after setting localizationConfig
		this.previewStep = new PreviewStep<>(this);
		
		this.initUI();
		this.registerListeners();
		
		this.wizardPanel.setState(initialState);
	}
	
	protected void initUI()
	{
		this.setHeaderTitle(this.translate(GridExportLocalizationConfig.EXPORT_GRID));
		this.closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		this.getHeader().add(this.closeButton);
		
		this.addStepsToWizardPanel();
		this.add(this.wizardPanel);
		
		this.buttonBar.withButtonText(
			WizardButtonBarWithAnchor::getBtnCancel,
			this.translate(GridExportLocalizationConfig.CANCEL));
		this.buttonBar.withButtonText(
			WizardButtonBarWithAnchor::getBtnPrevious,
			this.translate(GridExportLocalizationConfig.PREVIOUS));
		this.buttonBar.withButtonText(
			WizardButtonBarWithAnchor::getBtnNext,
			this.translate(GridExportLocalizationConfig.NEXT));
		this.buttonBar.withButtonText(
			WizardButtonBarWithAnchor::getBtnDone,
			this.translate(GridExportLocalizationConfig.DOWNLOAD));
		
		this.buttonBar.getAnchorDone().getElement().setAttribute("download", true);
		this.buttonBar.getBtnDone().setIcon(VaadinIcon.DOWNLOAD.create());
		this.getFooter().add(this.buttonBar);
		
		this.setResizable(true);
		this.setDraggable(true);
		
		this.setWidth("80%");
		this.setMaxWidth("70em");
		
		this.setHeight("90%");
		this.setMaxHeight("70em");
	}
	
	protected void addStepsToWizardPanel()
	{
		this.wizardPanel.addStep(new GeneralStep<>(this));
		this.wizardPanel.addStep(new FormatStep<>(this));
		this.wizardPanel.addStep(this.previewStep);
	}
	
	protected void registerListeners()
	{
		this.closeButton.addClickListener(ev -> this.close());
		
		this.buttonBar.addCancelClickListener(ev -> this.close());
		
		// Add the StreamResource to the download button when the preview is shown
		this.previewStep.addStreamResourceGeneratedListener(ev ->
			this.buttonBar.getAnchorDone().setHref(ev.getResource()));
		// Free up StreamResource when the user leaves the preview (navigates back)
		this.wizardPanel.addStepStateChangedListener(ev -> {
			if(!ev.isLastStep() && !this.buttonBar.getAnchorDone().getHref().isBlank())
			{
				this.buttonBar.getAnchorDone().setHref("");
			}
		});
	}
	
	@Override
	public void afterNavigation(final AfterNavigationEvent event)
	{
		// Workaround for https://github.com/vaadin/flow-components/issues/1541
		this.close();
	}
	
	@Override
	public String translate(final String key)
	{
		return this.localizationConfig.getTranslation(key, this);
	}
}
