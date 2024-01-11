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
	 * @return <code>false</code> when the exit can't happen due to e.g. validation problems.
	 */
	default boolean onProgress(final S state)
	{
		// optional
		return true;
	}
}
