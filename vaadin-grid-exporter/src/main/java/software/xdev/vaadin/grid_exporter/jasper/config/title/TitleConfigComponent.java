package software.xdev.vaadin.grid_exporter.jasper.config.title;

import com.vaadin.flow.component.textfield.TextField;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


public class TitleConfigComponent extends SpecificConfigComponent<TitleConfig>
{
	protected final TextField txtTitle = new TextField();
	
	public TitleConfigComponent(final Translator translator)
	{
		super(translator, TitleConfig::new, JasperConfigsLocalization.TITLE);
		
		this.initUIs();
		
		this.registerBindings();
	}
	
	protected void initUIs()
	{
		this.getContent().add(this.txtTitle);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.txtTitle)
			.bind(TitleConfig::getTitle, TitleConfig::setTitle);
	}
}
