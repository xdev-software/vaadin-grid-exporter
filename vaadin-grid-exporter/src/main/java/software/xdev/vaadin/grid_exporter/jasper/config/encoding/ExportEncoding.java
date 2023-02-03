package software.xdev.vaadin.grid_exporter.jasper.config.encoding;

import java.nio.charset.Charset;


public class ExportEncoding
{
	protected final Charset charset;
	protected final String bom;
	
	public ExportEncoding(final Charset charset)
	{
		this(charset, null);
	}
	
	public ExportEncoding(final Charset charset, final String bom)
	{
		this.charset = charset;
		this.bom = bom;
	}
	
	public Charset charset()
	{
		return this.charset;
	}
	
	public String bom()
	{
		return this.bom;
	}
	
	public boolean hasBom()
	{
		return this.bom() != null;
	}
}
