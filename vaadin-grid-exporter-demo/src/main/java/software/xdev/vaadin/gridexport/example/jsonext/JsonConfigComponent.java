package software.xdev.vaadin.gridexport.example.jsonext;

import static java.util.Map.entry;

import java.util.Map;

import com.vaadin.flow.component.checkbox.Checkbox;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;


public class JsonConfigComponent extends SpecificConfigComponent<JsonConfig>
{
	public static final String JSON = "json";
	
	public static final String USE_PRETTY_PRINT = "use_pretty_print";
	
	public static final String WITH_KEYS = "with_keys";
	
	// Key, Default Value
	public static final Map<String, String> DEFAULT_VALUES = Map.ofEntries(
		entry(JSON, "JSON"),
		entry(USE_PRETTY_PRINT, "Use pretty print"),
		entry(WITH_KEYS, "Export with keys")
	);
	
	protected final Checkbox chbxUsePrettyPrint = new Checkbox();
	
	protected final Checkbox chbxWithKeys = new Checkbox();
	
	protected JsonConfigComponent(final Translator translator)
	{
		super(translator, JsonConfig::new, JSON);
		
		this.initUI();
		
		this.registerBindings();
	}
	
	protected void initUI()
	{
		this.chbxUsePrettyPrint.setLabel(this.translate(USE_PRETTY_PRINT));
		
		this.chbxWithKeys.setLabel(this.translate(WITH_KEYS));
		
		this.getContent().add(this.chbxUsePrettyPrint, this.chbxWithKeys);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.chbxUsePrettyPrint)
			.bind(JsonConfig::isUsePrettyPrint, JsonConfig::setUsePrettyPrint);
		
		this.binder.forField(this.chbxWithKeys)
			.bind(JsonConfig::isWithKeys, JsonConfig::setWithKeys);
	}
}
