package software.xdev.vaadin.grid_exporter.jasper.config.header;

import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class HeaderConfig implements SpecificConfig
{
	protected boolean exportHeader;
	
	public HeaderConfig()
	{
		this(true);
	}
	
	public HeaderConfig(final boolean exportHeader)
	{
		this.exportHeader = exportHeader;
	}
	
	public boolean isExportHeader()
	{
		return this.exportHeader;
	}
	
	public void setExportHeader(final boolean exportHeader)
	{
		this.exportHeader = exportHeader;
	}
}
