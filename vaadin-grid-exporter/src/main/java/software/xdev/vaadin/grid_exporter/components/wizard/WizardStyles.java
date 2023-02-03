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
