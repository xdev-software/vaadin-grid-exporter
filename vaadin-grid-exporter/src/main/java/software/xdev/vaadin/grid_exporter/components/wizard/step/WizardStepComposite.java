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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

import software.xdev.vaadin.grid_exporter.components.wizard.WizardState;

@SuppressWarnings("java:S1948")
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
