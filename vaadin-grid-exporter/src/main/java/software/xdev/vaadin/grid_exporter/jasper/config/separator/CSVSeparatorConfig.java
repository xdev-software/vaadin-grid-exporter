package software.xdev.vaadin.grid_exporter.jasper.config.separator;

import java.util.Objects;

import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class CSVSeparatorConfig implements SpecificConfig
{
	protected String separator = ";";
	
	public String getSeparator()
	{
		return this.separator;
	}
	
	public void setSeparator(final String separator)
	{
		Objects.requireNonNull(separator);
		if(separator.isEmpty())
		{
			throw new IllegalArgumentException("Separator can't be empty");
		}
		this.separator = separator;
	}
}
