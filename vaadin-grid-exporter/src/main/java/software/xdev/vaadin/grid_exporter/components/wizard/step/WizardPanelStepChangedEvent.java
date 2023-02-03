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
