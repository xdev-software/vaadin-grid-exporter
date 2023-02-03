package software.xdev.vaadin.grid_exporter.jasper.config.highlight;

import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class HighlightConfig implements SpecificConfig
{
	protected boolean highlightOddRows = false;
	
	public boolean isHighlightOddRows()
	{
		return this.highlightOddRows;
	}
	
	public void setHighlightOddRows(final boolean highlightOddRows)
	{
		this.highlightOddRows = highlightOddRows;
	}
}
