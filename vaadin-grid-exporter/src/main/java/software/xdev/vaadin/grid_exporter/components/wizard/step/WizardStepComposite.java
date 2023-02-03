package software.xdev.vaadin.grid_exporter.components.wizard.step;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

import software.xdev.vaadin.grid_exporter.components.wizard.WizardState;


public abstract class WizardStepComposite<C extends Component, S extends WizardState>
	extends Composite<C>
	implements WizardStep<S>
{
	protected String stepName = "";
	protected S state = null;
	
	protected WizardStepComposite()
	{
		this.setStepName(this.getClass().getSimpleName());
	}
	
	/*
	 * OVERRIDES
	 */
	
	@Override
	public String getStepName()
	{
		return this.stepName;
	}
	
	public void setStepName(final String stepName)
	{
		this.stepName = stepName;
	}
	
	public S getWizardState()
	{
		return this.state;
	}
	
	@Override
	public void setWizardState(final S state)
	{
		this.state = state;
	}
}
