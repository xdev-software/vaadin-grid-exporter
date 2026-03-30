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
