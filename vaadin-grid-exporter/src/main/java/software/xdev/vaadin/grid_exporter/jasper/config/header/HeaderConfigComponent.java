package software.xdev.vaadin.grid_exporter.jasper.config.header;

import com.vaadin.flow.component.checkbox.Checkbox;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


public class HeaderConfigComponent extends SpecificConfigComponent<HeaderConfig>
{
	protected final Checkbox chbxExportHeader = new Checkbox();
	
	public HeaderConfigComponent(final Translator translator, final boolean defaultExportHeaderValue)
	{
		super(translator, () -> new HeaderConfig(defaultExportHeaderValue), JasperConfigsLocalization.HEADER);
		
		this.initUI();
		
		this.registerBindings();
	}
	
	public HeaderConfigComponent(final Translator translator)
	{
		this(translator, true);
	}
	
	protected void initUI()
	{
		this.chbxExportHeader.setLabel(this.translate(JasperConfigsLocalization.EXPORT_HEADER));
		
		this.getContent().add(this.chbxExportHeader);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.chbxExportHeader)
			.bind(HeaderConfig::isExportHeader, HeaderConfig::setExportHeader);
	}
}
