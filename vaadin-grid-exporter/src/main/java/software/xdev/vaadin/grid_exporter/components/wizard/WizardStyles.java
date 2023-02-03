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
package software.xdev.vaadin.grid_exporter.components.wizard;

public final class WizardStyles
{
	private WizardStyles()
	{
		// No impl
	}
	
	public static final String LOCATION = "./styles/wizard.css";
	
	// region WizardPanel
	
	public static final String WIZARD_PANEL = "wizard-panel";
	
	public static final String WIZARD_PANEL_TABS = WIZARD_PANEL + "-tabs";
	
	public static final String WIZARD_PANEL_CONTENT = WIZARD_PANEL + "-content";
	
	// endregion
	// region WizardButtonBar
	
	public static final String WIZARD_BUTTON_BAR = "wizard-button-bar";
	
	public static final String WIZARD_BUTTON_BAR_BTN_CANCEL = WIZARD_BUTTON_BAR + "-cancel";
	public static final String WIZARD_BUTTON_BAR_BTN_PREVIOUS = WIZARD_BUTTON_BAR + "-previous";
	public static final String WIZARD_BUTTON_BAR_BTN_NEXT = WIZARD_BUTTON_BAR + "-next";
	public static final String WIZARD_BUTTON_BAR_BTN_DONE = WIZARD_BUTTON_BAR + "-done";
	
	// endregion
}
