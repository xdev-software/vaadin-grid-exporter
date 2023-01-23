/*
 * Copyright © 2022 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	private String title = ""; // No text = no title
	
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
	
	public String getTitle()
	{
		return this.title;
	}
	
	public PdfSpecificConfig withTitle(final String title)
	{
		this.title = title;
		return this;
	}
}
