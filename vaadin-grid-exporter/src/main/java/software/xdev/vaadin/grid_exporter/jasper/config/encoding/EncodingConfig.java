package software.xdev.vaadin.grid_exporter.jasper.config.encoding;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class EncodingConfig implements SpecificConfig
{
	protected List<ExportEncoding> available = Arrays.asList(
		new ExportEncoding(StandardCharsets.UTF_8, "\ufeff"),
		new ExportEncoding(StandardCharsets.ISO_8859_1)
	);
	
	protected ExportEncoding selected = this.available.get(0);
	
	protected boolean useBOM = false;
	
	public List<ExportEncoding> getAvailable()
	{
		return this.available;
	}
	
	public void setAvailable(final List<ExportEncoding> available)
	{
		this.available = Objects.requireNonNull(available);
	}
	
	public ExportEncoding getSelected()
	{
		return this.selected;
	}
	
	public void setSelected(final ExportEncoding selected)
	{
		this.selected = selected;
	}
	
	public boolean isUseBOM()
	{
		return this.useBOM;
	}
	
	public void setUseBOM(final boolean useBOM)
	{
		this.useBOM = useBOM;
	}
	
	
	public boolean supportsAndUseBOM()
	{
		return this.isUseBOM() && this.getSelected().hasBom();
	}
}
