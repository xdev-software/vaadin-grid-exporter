package software.xdev.vaadin.grid_exporter.jasper.config;

import static java.util.Map.entry;

import java.util.Map;


public final class JasperConfigsLocalization
{
	private JasperConfigsLocalization()
	{
		// No impl
	}
	
	public static final String PREFIX = "gridexporter.jasperformats.";
	
	public static final String ENCODING = PREFIX + "encoding";
	public static final String WITH_BOM = PREFIX + "with_bom";
	
	
	public static final String HEADER = PREFIX + "header";
	public static final String EXPORT_HEADER = PREFIX + "export_header";
	
	public static final String HIGHLIGHTING = PREFIX + "highlighting";
	public static final String HIGHLIGHT_ROWS = PREFIX + "highlight_rows";
	
	public static final String PAGE = PREFIX + "page";
	public static final String FORMAT_PAGE_TYPE = PREFIX + "format_page_type";
	public static final String ORIENTATION = PREFIX + "orientation";
	public static final String ORIENTATION_PORTRAIT = ORIENTATION + ".portrait";
	public static final String ORIENTATION_LANDSCAPE = ORIENTATION + ".landscape";
	public static final String SHOW_PAGE_NUMBERS = PREFIX + "show_page_numbers";
	public static final String MARGIN = PREFIX + "margin";
	
	public static final String SEPARATOR = PREFIX + "separator";
	
	public static final String TITLE = PREFIX + "title";
	
	// Key, Default Value
	public static final Map<String, String> DEFAULT_VALUES = Map.ofEntries(
		entry(ENCODING, "Encoding"),
		entry(WITH_BOM, "with BOM"),
		entry(HEADER, "Header"),
		entry(EXPORT_HEADER, "Export header"),
		entry(HIGHLIGHTING, "Highlighting"),
		entry(HIGHLIGHT_ROWS, "Highlight rows"),
		entry(PAGE, "Page"),
		entry(FORMAT_PAGE_TYPE, "Format / Page type"),
		entry(ORIENTATION, "Orientation"),
		entry(ORIENTATION_PORTRAIT, "Portrait"),
		entry(ORIENTATION_LANDSCAPE, "Landscape"),
		entry(SHOW_PAGE_NUMBERS, "Show page numbers"),
		entry(MARGIN, "Margin"),
		entry(SEPARATOR, "Separator"),
		entry(TITLE, "Title")
	);
}
