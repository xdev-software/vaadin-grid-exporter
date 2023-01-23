package software.xdev.vaadin.grid_exporter.format.jasper.pdf;

import java.awt.Insets;

import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class PdfSpecificConfig implements SpecificConfig
{
	private PageType pageType = PageType.A4;
	private PageOrientation pageOrientation = PageOrientation.PORTRAIT;
	private Insets pageMargin = new Insets(20, 20, 20, 20);
	private boolean showPageNumber = false;
	private boolean highlightRows = false;
	
	public PageType getPageType()
	{
		return this.pageType;
	}
	
	public PdfSpecificConfig withPageType(final PageType pageType)
	{
		this.pageType = pageType;
		return this;
	}
	
	public PageOrientation getPageOrientation()
	{
		return this.pageOrientation;
	}
	
	public PdfSpecificConfig withPageOrientation(final PageOrientation pageOrientation)
	{
		this.pageOrientation = pageOrientation;
		return this;
	}
	
	public Insets getPageMargin()
	{
		return this.pageMargin;
	}
	
	public PdfSpecificConfig withPageMargin(final Insets pageMargin)
	{
		this.pageMargin = pageMargin;
		return this;
	}
	
	public boolean isShowPageNumber()
	{
		return this.showPageNumber;
	}
	
	public PdfSpecificConfig withShowPageNumber(final boolean showPageNumber)
	{
		this.showPageNumber = showPageNumber;
		return this;
	}
	
	public boolean isHighlightRows()
	{
		return this.highlightRows;
	}
	
	public PdfSpecificConfig withHighlightRows(final boolean highlightRows)
	{
		this.highlightRows = highlightRows;
		return this;
	}
}
