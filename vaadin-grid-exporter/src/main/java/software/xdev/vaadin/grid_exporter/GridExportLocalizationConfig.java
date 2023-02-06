/*
 * Copyright © 2022 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.xdev.vaadin.grid_exporter;

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.vaadin.flow.component.Component;


/**
 * Makes i18n possible for the component. As default the english language is used.
 *
 * @author AB
 */
public class GridExportLocalizationConfig
{
	public static final String PREFIX = "gridexporter.";
	
	public static final String EXPORT_GRID = PREFIX + "export_grid";
	
	public static final String CANCEL = PREFIX + "cancel";
	public static final String PREVIOUS = PREFIX + "previous";
	public static final String NEXT = PREFIX + "next";
	public static final String DOWNLOAD = PREFIX + "download";
	
	public static final String GENERAL = PREFIX + "general";
	public static final String FILENAME = PREFIX + "filename";
	public static final String COLUMNS = PREFIX + "columns";
	public static final String NAME = PREFIX + "name";
	public static final String POSITION = PREFIX + "position";
	public static final String ALREADY_PRESENT = PREFIX + "already_present";
	
	public static final String FORMAT = PREFIX + "format";
	
	public static final String PREVIEW = PREFIX + "preview";
	
	public static final String UNABLE_TO_SHOW_PREVIEW = PREFIX + "unable_to_show_preview";
	
	// Key, Default Value
	public static final Map<String, String> DEFAULT_VALUES = Map.ofEntries(
		entry(EXPORT_GRID, "Export Grid"),
		// Buttons
		entry(CANCEL, "Cancel"),
		entry(PREVIOUS, "Previous"),
		entry(NEXT, "Next"),
		entry(DOWNLOAD, "Download"),
		// Steps
		entry(GENERAL, "General"),
		entry(FILENAME, "Filename"),
		entry(COLUMNS, "Columns"),
		entry(NAME, "Name"),
		entry(POSITION, "Position"),
		entry(ALREADY_PRESENT, "Already present"),
		entry(FORMAT, "Format"),
		entry(PREVIEW, "Preview"),
		entry(UNABLE_TO_SHOW_PREVIEW, "Unable to show preview")
	);
	
	// Key, Resolver
	protected final Map<String, BiFunction<Component, String, String>> keyResolvers = new HashMap<>();
	
	public GridExportLocalizationConfig withAll(final Map<String, String> keyValues)
	{
		keyValues.forEach(this::with);
		return this;
	}
	
	public GridExportLocalizationConfig with(final String key, final String value)
	{
		this.getKeyResolvers().put(key, (c, k) -> value);
		return this;
	}
	
	public GridExportLocalizationConfig withTranslation(final String key, final String i18nKey)
	{
		this.getKeyResolvers().put(
			key,
			(c, k) -> c.getTranslation(i18nKey));
		return this;
	}
	
	public String getTranslation(final String key, final Component caller)
	{
		final BiFunction<Component, String, String> resolver = this.getKeyResolvers().get(key);
		if(resolver == null)
		{
			final String defaultValue = DEFAULT_VALUES.get(key);
			return defaultValue != null ? defaultValue : key;
		}
		return resolver.apply(caller, key);
	}
	
	public Map<String, BiFunction<Component, String, String>> getKeyResolvers()
	{
		return this.keyResolvers;
	}
}
