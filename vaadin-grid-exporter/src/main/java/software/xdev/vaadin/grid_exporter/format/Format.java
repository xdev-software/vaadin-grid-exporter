package software.xdev.vaadin.grid_exporter.format;

import java.io.OutputStream;

import com.vaadin.flow.component.grid.Grid;


public interface Format<T, E extends SpecificConfig>
{
	String getFormatNameToDisplay();
	
	String getFormatFilenameSuffix();
	
	String getMimeType();
	
	/**
	 * Returns if a preview can be shown in a "standardized" browser (e.g. the latest versions of Chrome and Firefox)
	 *
	 * @return <code>true</code> if a preview can be shown in the browser
	 */
	boolean isPreviewableInStandardBrowser();
	
	FormatConfigComponent<E> getConfigurationComponent();
	
	OutputStream export(Grid<T> gridToExport, GeneralConfig<T> generalConfig, E specificConfig);
	
	default OutputStream export(final Grid<T> gridToExport, final GeneralConfig<T> generalConfig)
	{
		return this.export(gridToExport, generalConfig, this.getConfigurationComponent().getConfig());
	}
	
}
