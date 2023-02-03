package software.xdev.vaadin.grid_exporter.components.wizard.panel;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;

import software.xdev.vaadin.grid_exporter.components.wizard.WizardState;
import software.xdev.vaadin.grid_exporter.components.wizard.WizardStyles;
import software.xdev.vaadin.grid_exporter.components.wizard.step.WizardPanelStepChangedEvent;
import software.xdev.vaadin.grid_exporter.components.wizard.step.WizardStep;
import software.xdev.vaadin.grid_exporter.components.wizard.step.WizardStepState;


@CssImport(WizardStyles.LOCATION)
public class WizardPanel<S extends WizardState>
	extends Composite<VerticalLayout>
	implements WizardPanelActions
{
	protected int stepIndex = -1;
	protected final List<Component> lstSteps = new ArrayList<>();
	protected final Map<Tab, Component> tabStepMap = new HashMap<>();
	
	protected S curState = null;
	
	protected final Tabs tabsStep = new Tabs();
	protected final ProgressBar progress = new ProgressBar();
	protected final VerticalLayout vlContent = new VerticalLayout();
	public WizardPanel()
	{
		this.initUI();
	}
	
	protected void initUI()
	{
		this.tabsStep.addClassName(WizardStyles.WIZARD_PANEL_TABS);
		this.tabsStep.setAutoselect(false);
		this.tabsStep.addSelectedChangeListener(this::onTabChange);
		
		this.progress.getStyle().set("margin-top", "0");
		
		this.vlContent.addClassName(WizardStyles.WIZARD_PANEL_CONTENT);
		this.vlContent.getStyle().set("overflow-y", "auto");
		this.vlContent.setPadding(false);
		
		
		final VerticalLayout vlRoot = this.getContent();
		vlRoot.addClassName(WizardStyles.WIZARD_PANEL);
		vlRoot.setPadding(false);
		vlRoot.setSpacing(false);
		vlRoot.setAlignItems(Alignment.STRETCH);
		
		vlRoot.add(this.tabsStep);
		vlRoot.add(this.progress);
		vlRoot.addAndExpand(this.vlContent);
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		if(!this.lstSteps.isEmpty())
		{
			this.showFirstStep(attachEvent.isFromClient());
		}
	}
	
	protected void onTabChange(final SelectedChangeEvent event)
	{
		final Tab selTab = event.getSelectedTab();
		final Component tabPage = this.tabStepMap.get(selTab);
		this.stepIndex = this.lstSteps.indexOf(tabPage);
		
		this.vlContent.removeAll();
		this.vlContent.add(tabPage);
		
		this.updateProgress();
	}
	
	public <T extends Component & WizardStep<S>> void addStep(final T step)
	{
		requireNonNull(step);
		
		final Tab tab = new Tab(step.getStepName());
		tab.setFlexGrow(1);
		tab.setEnabled(false);
		
		this.tabStepMap.put(tab, step);
		this.lstSteps.add(step);
		this.tabsStep.add(tab);
	}
	
	public S getState()
	{
		return this.curState;
	}
	
	@SuppressWarnings("unchecked")
	public void setState(final S state)
	{
		this.curState = state;
		this.lstSteps.forEach(step -> ((WizardStep<S>)step).setWizardState(state));
	}
	
	protected void updateProgress()
	{
		final int curStep = this.stepIndex + 1;
		final int stepCount = this.lstSteps.size();
		
		this.progress.setValue(stepCount == 0
			? 1d
			: (double)curStep / (double)stepCount);
	}
	@Override
	public void showFirstStep(final boolean isFromClient)
	{
		this.showStep(0, isFromClient);
	}
	
	@Override
	public void showPreviousStep(final boolean isFromClient)
	{
		this.showStep(this.stepIndex - 1, isFromClient);
	}
	
	@Override
	public void showNextStep(final boolean isFromClient)
	{
		@SuppressWarnings("unchecked")
		final WizardStep<S> curStep = (WizardStep<S>)this.lstSteps.get(this.stepIndex);
		
		if(!curStep.onProgress(this.curState))
		{
			return;
		}
		
		if(this.lstSteps.isEmpty() || this.stepIndex == this.lstSteps.size() - 1)
		{
			return;
		}
		
		if(this.stepIndex >= 0 && this.stepIndex < this.lstSteps.size() - 1)
		{
			final int nextStepIndex = this.stepIndex + 1;
			this.showStep(nextStepIndex, isFromClient);
		}
	}
	
	protected void showStep(final int stepIndex, final boolean isFromClient)
	{
		if(stepIndex < 0
			|| stepIndex >= this.lstSteps.size()
			|| this.stepIndex == stepIndex)
		{
			return;
		}
		
		this.enableTab(this.stepIndex, false);
		this.enableTab(stepIndex, true);
		
		this.stepIndex = stepIndex;
		this.tabsStep.setSelectedIndex(stepIndex);
		
		@SuppressWarnings("unchecked")
		final WizardStep<S> newStep = (WizardStep<S>)this.lstSteps.get(stepIndex);
		newStep.onEnterStep(this.curState);
		
		this.updateProgress();
		
		// FireUpdate
		this.fireEvent(
			new WizardPanelStepChangedEvent<>(
				new WizardStepState(this.stepIndex + 1, this.lstSteps.size()),
				this,
				isFromClient));
	}
	
	protected void enableTab(final int stepIndex, final boolean enable)
	{
		if(stepIndex < 0 || stepIndex >= this.tabsStep.getComponentCount())
		{
			return;
		}
		
		final Component tab = this.tabsStep.getComponentAt(stepIndex);
		if(tab instanceof HasEnabled)
		{
			((HasEnabled)tab).setEnabled(enable);
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void addStepStateChangedListener(final Consumer<WizardStepState> newStateConsumer)
	{
		this.addListener(WizardPanelStepChangedEvent.class,
			(ComponentEventListener)(ComponentEventListener<WizardPanelStepChangedEvent<S>>)
				ev -> newStateConsumer.accept(ev.getStepState()));
	}
}
