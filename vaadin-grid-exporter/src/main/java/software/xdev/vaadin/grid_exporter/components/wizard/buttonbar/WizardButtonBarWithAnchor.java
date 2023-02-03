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
