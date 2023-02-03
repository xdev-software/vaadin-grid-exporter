package software.xdev.vaadin.grid_exporter.jasper.config.title;

import java.util.Objects;

import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class TitleConfig implements SpecificConfig
{
	protected String title;
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle(final String title)
	{
		this.title = Objects.requireNonNull(title);
	}
	
	public boolean notTitleEmpty()
	{
		return this.getTitle() != null && !this.getTitle().isBlank();
	}
}
