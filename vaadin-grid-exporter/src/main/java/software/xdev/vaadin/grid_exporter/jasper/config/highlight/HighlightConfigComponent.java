package software.xdev.vaadin.grid_exporter.jasper.config.highlight;

import com.vaadin.flow.component.checkbox.Checkbox;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


public class HighlightConfigComponent extends SpecificConfigComponent<HighlightConfig>
{
	protected final Checkbox chbxExportHeader = new Checkbox();
	
	public HighlightConfigComponent(final Translator translator)
	{
		super(translator, HighlightConfig::new, JasperConfigsLocalization.HIGHLIGHTING);
		
		this.initUI();
		
		this.registerBindings();
	}
	
	protected void initUI()
	{
		this.chbxExportHeader.setLabel(this.translate(JasperConfigsLocalization.HIGHLIGHT_ROWS));
		
		this.getContent().add(this.chbxExportHeader);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.chbxExportHeader)
			.bind(HighlightConfig::isHighlightOddRows, HighlightConfig::setHighlightOddRows);
	}
}
