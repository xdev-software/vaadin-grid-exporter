package software.xdev.vaadin.grid_exporter.components.wizard.step;

public class WizardStepState
{
	protected final int currentStep;
	
	protected final int totalSteps;
	
	public WizardStepState(final int currentStep, final int totalSteps)
	{
		if(totalSteps < 0)
		{
			throw new IllegalArgumentException("Total steps is invalid");
		}
		if(currentStep < 0 || currentStep > totalSteps)
		{
			throw new IllegalArgumentException("Current step is invalid");
		}
		
		this.currentStep = currentStep;
		this.totalSteps = totalSteps;
	}
	
	public int getCurrentStep()
	{
		return this.currentStep;
	}
	
	public int getTotalSteps()
	{
		return this.totalSteps;
	}
	
	public boolean isFirstStep()
	{
		return this.getCurrentStep() <= 1;
	}
	
	public boolean isLastStep()
	{
		return this.getCurrentStep() >= this.getTotalSteps();
	}
}
