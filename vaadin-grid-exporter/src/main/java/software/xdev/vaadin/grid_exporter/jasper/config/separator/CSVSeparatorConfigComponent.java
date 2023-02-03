package software.xdev.vaadin.grid_exporter.jasper.config.separator;

import com.vaadin.flow.component.textfield.TextField;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


public class CSVSeparatorConfigComponent extends SpecificConfigComponent<CSVSeparatorConfig>
{
	protected final TextField txtSeparator = new TextField();
	
	public CSVSeparatorConfigComponent(final Translator translator)
	{
		super(translator, CSVSeparatorConfig::new, JasperConfigsLocalization.SEPARATOR);
		
		this.initUIs();
		
		this.registerBindings();
	}
	
	protected void initUIs()
	{
		this.getContent().add(this.txtSeparator);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.txtSeparator)
			.asRequired()
			.bind(CSVSeparatorConfig::getSeparator, CSVSeparatorConfig::setSeparator);
	}
}
