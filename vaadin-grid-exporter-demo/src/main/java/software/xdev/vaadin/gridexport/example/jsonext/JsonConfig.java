package software.xdev.vaadin.gridexport.example.jsonext;

import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class JsonConfig implements SpecificConfig
{
	protected boolean usePrettyPrint = true;
	
	protected boolean withKeys = true;
	
	public boolean isUsePrettyPrint()
	{
		return this.usePrettyPrint;
	}
	
	public void setUsePrettyPrint(final boolean usePrettyPrint)
	{
		this.usePrettyPrint = usePrettyPrint;
	}
	
	public boolean isWithKeys()
	{
		return this.withKeys;
	}
	
	public void setWithKeys(final boolean withKeys)
	{
		this.withKeys = withKeys;
	}
}
