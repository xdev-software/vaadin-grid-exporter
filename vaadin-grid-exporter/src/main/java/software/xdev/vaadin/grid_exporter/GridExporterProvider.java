package software.xdev.vaadin.grid_exporter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import software.xdev.vaadin.grid_exporter.format.Format;


/**
 * Provides data to the {@link GridExporter}.
 * <p>
 * Can be used for extensions.
 * </p>
 */
public class GridExporterProvider
{
	protected final Map<String, String> defaultTranslationKeyValues;
	
	protected final List<Format> formats;
	
	public GridExporterProvider(final Map<String, String> defaultTranslationKeyValues, final Format... formats)
	{
		this(defaultTranslationKeyValues, Arrays.asList(formats));
	}
	
	public GridExporterProvider(final Map<String, String> defaultTranslationKeyValues, final List<Format> formats)
	{
		this.defaultTranslationKeyValues = defaultTranslationKeyValues;
		this.formats = formats;
	}
	
	public Map<String, String> getDefaultTranslationKeyValues()
	{
		return this.defaultTranslationKeyValues;
	}
	
	public List<Format> getFormats()
	{
		return this.formats;
	}
}
