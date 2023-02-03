package software.xdev.vaadin.grid_exporter.components.wizard.step;

import software.xdev.vaadin.grid_exporter.components.wizard.WizardState;


public interface WizardStep<S extends WizardState>
{
	String getStepName();
	
	void setWizardState(S state);
	
	default void onEnterStep(final S state)
	{
		// optional
	}
	
	/**
	 * Called when next is clicked and the current step is exited
	 * @param state The current state
	 * @return <code>false</code> when the exit can't happen due to e.g. validation problems. Otherwise <code>true</code>.
	 */
	default boolean onProgress(final S state)
	{
		// optional
		return true;
	}
}
