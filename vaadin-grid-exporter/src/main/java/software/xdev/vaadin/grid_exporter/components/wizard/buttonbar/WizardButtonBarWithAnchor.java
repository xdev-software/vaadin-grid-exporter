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

import com.vaadin.flow.component.html.Anchor;

import software.xdev.vaadin.grid_exporter.components.wizard.panel.WizardPanelActions;
import software.xdev.vaadin.grid_exporter.components.wizard.step.WizardStepState;


public class WizardButtonBarWithAnchor extends AbstractWizardButtonBar<WizardButtonBarWithAnchor>
{
	protected final Anchor anchorDone = new Anchor();
	
	public WizardButtonBarWithAnchor(final WizardPanelActions panel)
	{
		this.init(panel);
	}
	
	@Override
	protected void initUI()
	{
		super.initUI();
		
		this.hlEndButtons.remove(this.btnDone);
		this.hlEndButtons.add(this.anchorDone);
		
		this.btnDone.setDisableOnClick(false);
		this.anchorDone.add(this.btnDone);
	}
	
	@Override
	protected void updateFromStepState(final WizardStepState stepState)
	{
		super.updateFromStepState(stepState);
		
		this.getAnchorDone().setVisible(this.getBtnDone().isVisible());
	}
	
	public Anchor getAnchorDone()
	{
		return this.anchorDone;
	}
}
