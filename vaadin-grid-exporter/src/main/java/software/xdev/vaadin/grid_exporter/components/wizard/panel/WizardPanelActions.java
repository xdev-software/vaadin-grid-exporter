package software.xdev.vaadin.grid_exporter.components.wizard.panel;

import java.util.function.Consumer;

import software.xdev.vaadin.grid_exporter.components.wizard.step.WizardStepState;


public interface WizardPanelActions
{
	void showFirstStep(final boolean isFromClient);
	
	void showPreviousStep(final boolean isFromClient);
	
	void showNextStep(final boolean isFromClient);
	
	void addStepStateChangedListener(final Consumer<WizardStepState> newStateConsumer);
}
