package software.xdev.vaadin.grid_exporter.wizard.steps;

import java.util.Objects;

import com.vaadin.flow.component.Component;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.components.wizard.GridExporterWizardState;
import software.xdev.vaadin.grid_exporter.components.wizard.step.WizardStepComposite;


public abstract class AbstractGridExportWizardStepComposite<C extends Component, T>
	extends WizardStepComposite<C, GridExporterWizardState<T>>
	implements Translator
{
	protected Translator translator;
	
	protected AbstractGridExportWizardStepComposite(final Translator translator)
	{
		this.translator = Objects.requireNonNull(translator);
	}
	
	@Override
	public String translate(final String key)
	{
		return this.translator.translate(key);
	}
}
