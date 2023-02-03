package software.xdev.vaadin.grid_exporter.components.wizard.step;

import java.util.Objects;

import com.vaadin.flow.component.ComponentEvent;

import software.xdev.vaadin.grid_exporter.components.wizard.WizardState;
import software.xdev.vaadin.grid_exporter.components.wizard.panel.WizardPanel;


public class WizardPanelStepChangedEvent<S extends WizardState> extends ComponentEvent<WizardPanel<S>>
{
	protected final WizardStepState stepState;
	
	public WizardPanelStepChangedEvent(
		final WizardStepState stepState,
		final WizardPanel<S> source,
		final boolean fromClient)
	{
		super(source, fromClient);
		this.stepState = Objects.requireNonNull(stepState);
	}
	
	public WizardStepState getStepState()
	{
		return this.stepState;
	}
}
