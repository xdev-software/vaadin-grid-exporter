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
package software.xdev.vaadin.grid_exporter.components.wizard.buttonbar;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import software.xdev.vaadin.grid_exporter.components.wizard.WizardStyles;
import software.xdev.vaadin.grid_exporter.components.wizard.panel.WizardPanelActions;
import software.xdev.vaadin.grid_exporter.components.wizard.step.WizardStepState;


@CssImport(WizardStyles.LOCATION)
public abstract class AbstractWizardButtonBar<P extends AbstractWizardButtonBar<P>> extends Composite<HorizontalLayout>
	implements HasSize, HasStyle
{
	protected final Button btnCancel = new Button("Cancel");
	protected final Button btnPrevious = new Button("Back");
	protected final Button btnNext = new Button("Next");
	protected final Button btnDone = new Button("Done");
	
	protected final HorizontalLayout hlEndButtons = new HorizontalLayout();
	
	protected void init(final WizardPanelActions panel)
	{
		Objects.requireNonNull(panel);
		
		this.initUI();
		this.registerListeners(panel);
		
		// Set initial state
		this.updateFromStepState(new WizardStepState(0, 0));
	}
	
	protected void initUI()
	{
		this.btnCancel.addClassName(WizardStyles.WIZARD_BUTTON_BAR_BTN_CANCEL);
		this.btnPrevious.addClassName(WizardStyles.WIZARD_BUTTON_BAR_BTN_PREVIOUS);
		this.btnNext.addClassName(WizardStyles.WIZARD_BUTTON_BAR_BTN_NEXT);
		this.btnDone.addClassName(WizardStyles.WIZARD_BUTTON_BAR_BTN_DONE);
		
		Stream.of(this.btnCancel, this.btnPrevious, this.btnNext, this.btnDone)
			.forEach(btn -> btn.setDisableOnClick(true));
		
		Stream.of(this.btnNext, this.btnDone)
			.forEach(btn -> btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY));
		
		this.hlEndButtons.setPadding(false);
		this.hlEndButtons.add(this.btnPrevious, this.btnNext, this.btnDone);
		
		this.getContent().addClassName(WizardStyles.WIZARD_BUTTON_BAR);
		this.getContent().setPadding(false);
		this.getContent().setWidthFull();
		this.getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		this.getContent().add(this.btnCancel, this.hlEndButtons);
	}
	
	protected void registerListeners(final WizardPanelActions panel)
	{
		this.addButtonClickEvent(this.getBtnPrevious(), panel::showPreviousStep);
		this.addButtonClickEvent(this.getBtnNext(), panel::showNextStep);
		
		panel.addStepStateChangedListener(this::updateFromStepState);
	}
	
	protected Registration addButtonClickEvent(final Button button, final Consumer<Boolean> isFromClientConsumer)
	{
		return button.addClickListener(ev ->
		{
			isFromClientConsumer.accept(ev.isFromClient());
			ev.getSource().setEnabled(true);
		});
	}
	
	protected void updateFromStepState(final WizardStepState stepState)
	{
		this.getBtnPrevious().setEnabled(!stepState.isFirstStep());
		
		this.getBtnNext().setVisible(!stepState.isLastStep());
		this.getBtnDone().setVisible(stepState.isLastStep());
	}
	
	// region Getter for Buttons
	
	public Button getBtnCancel()
	{
		return this.btnCancel;
	}
	
	public Button getBtnPrevious()
	{
		return this.btnPrevious;
	}
	
	public Button getBtnNext()
	{
		return this.btnNext;
	}
	
	public Button getBtnDone()
	{
		return this.btnDone;
	}
	
	// endregion
	
	public P configureButton(
		final Function<P, Button> selfButtonSupplier,
		final Consumer<Button> configureButtonAction)
	{
		configureButtonAction.accept(selfButtonSupplier.apply(this.self()));
		return this.self();
	}
	
	public P withButtonText(
		final Function<P, Button> selfButtonSupplier,
		final String text)
	{
		return this.configureButton(selfButtonSupplier, btn -> btn.setText(text));
	}
	
	public Registration addCancelClickListener(final Consumer<Boolean> isFromClientConsumer)
	{
		return this.addButtonClickEvent(this.getBtnCancel(), isFromClientConsumer);
	}
	
	public P withCancelClickListener(final Consumer<Boolean> isFromClientConsumer)
	{
		this.addCancelClickListener(isFromClientConsumer);
		return this.self();
	}
	
	public Registration addDoneClickListener(final Consumer<Boolean> isFromClientConsumer)
	{
		return this.addButtonClickEvent(this.getBtnDone(), isFromClientConsumer);
	}
	
	public P withDoneClickListener(final Consumer<Boolean> isFromClientConsumer)
	{
		this.addDoneClickListener(isFromClientConsumer);
		return this.self();
	}
	
	@SuppressWarnings("unchecked")
	protected P self()
	{
		return (P)this;
	}
}
