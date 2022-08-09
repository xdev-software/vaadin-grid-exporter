package com.rapidclipse.framework.server.reports.grid;

/*-
 * #%L
 * vaadin-grid-exporter
 * %%
 * Copyright (C) 2022 XDEV Software
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.vaadin.flow.component.Component;


public class GridExportLocalizationConfig
{
	public static final String PREFIX = "gridexporter.";
	
	public static final String EXPORT_CAPTION = PREFIX + "caption";
	public static final String REPORT_TITLE = PREFIX + "title";
	public static final String CONFIGURE_COLUMNS = PREFIX + "columns";
	public static final String NAME = PREFIX + "columnName";
	public static final String WIDTH = PREFIX + "columnWidth";
	public static final String POSITION = PREFIX + "position";
	public static final String SELECT_ALL = PREFIX + "selectAll";
	public static final String SELECT_NONE = PREFIX + "selectNone";
	public static final String PAGE_ORIENTATION = PREFIX + "orientation";
	public static final String SHOW_PAGE_NUMBERS = PREFIX + "showPageNumbers";
	public static final String HIGHLIGHT_ROWS = PREFIX + "highlightRows";
	public static final String PAGE_SIZE = PREFIX + "pageFormat";
	public static final String FORMAT = PREFIX + "format";
	public static final String WIDTH_TO_BIG_ERROR = PREFIX + "widthTooBigError";
	public static final String CANCEL = PREFIX + "cancel";
	public static final String EXPORT = PREFIX + "btnExport";
	
	public static final String PORTRAIT = PREFIX + "pageOrientation.portrait";
	public static final String LANDSCAPE = PREFIX + "pageOrientation.landscape";
	
	public static final String PREVIEW = PREFIX + "preview";
	public static final String UNABLE_TO_SHOW_PREVIEW = PREFIX + "unableToShowPreview";
	public static final String DOWNLOAD = PREFIX + "download";
	
	// Key, Default Value
	public static final Map<String, String > DEFAULT_VALUES = Map.ofEntries(
		entry(EXPORT_CAPTION, "Export Grid"),
		entry(REPORT_TITLE, "Report Title"),
		entry(CONFIGURE_COLUMNS, "Configure Columns"),
		entry(NAME, "Name"),
		entry(WIDTH, "Width"),
		entry(POSITION, "Position"),
		entry(SELECT_ALL, "Select All"),
		entry(SELECT_NONE, "Select None"),
		entry(PAGE_ORIENTATION, "Page Orientation"),
		entry(SHOW_PAGE_NUMBERS, "Show Page Numbers"),
		entry(HIGHLIGHT_ROWS, "Highlight Rows"),
		entry(PAGE_SIZE, "Page Size"),
		entry(FORMAT, "Format"),
		entry(WIDTH_TO_BIG_ERROR, "Column widths exceed page size"),
		entry(CANCEL, "Cancel"),
		entry(EXPORT, "Export"),
		entry(PORTRAIT, "Portrait"),
		entry(LANDSCAPE, "Landscape"),
		entry(PREVIEW, "Preview"),
		entry(UNABLE_TO_SHOW_PREVIEW, "Unable to show preview"),
		entry(DOWNLOAD, "Download")
	);
	
	// Key, Resolver
	protected final Map<String, BiFunction<Component, String, String>> keyResolvers = new HashMap<>();
	
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
			return DEFAULT_VALUES.get(key);
		}
		return resolver.apply(caller, key);
	}
	
	public Map<String, BiFunction<Component, String, String>> getKeyResolvers()
	{
		return this.keyResolvers;
	}
}
