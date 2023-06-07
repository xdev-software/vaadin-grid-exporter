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
package software.xdev.vaadin.grid_exporter.jasper.config.page;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import software.xdev.dynamicreports.report.constant.PageOrientation;
import software.xdev.dynamicreports.report.constant.PageType;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class PageConfig implements SpecificConfig
{
	public static final int PAGE_MARGIN_MIN = 0;
	public static final int PAGE_MARGIN_MAX = 1_000;
	protected List<PageType> availablePageTypes = Arrays.asList(PageType.values());
	
	protected List<PageOrientation> availablePageOrientations = Arrays.asList(PageOrientation.values());
	
	protected PageType selectedPageType = PageType.A4;
	
	protected PageOrientation selectedPageOrientation = PageOrientation.PORTRAIT;
	
	protected boolean usePageNumbering = false;
	
	protected int pageMargin = 20;
	
	public List<PageType> getAvailablePageTypes()
	{
		return this.availablePageTypes;
	}
	
	public void setAvailablePageTypes(final List<PageType> availablePageTypes)
	{
		this.availablePageTypes = Objects.requireNonNull(availablePageTypes);
	}
	
	public List<PageOrientation> getAvailablePageOrientations()
	{
		return this.availablePageOrientations;
	}
	
	public void setAvailablePageOrientations(final List<PageOrientation> availablePageOrientations)
	{
		this.availablePageOrientations = Objects.requireNonNull(availablePageOrientations);
	}
	
	public PageType getSelectedPageType()
	{
		return this.selectedPageType;
	}
	
	public void setSelectedPageType(final PageType selectedPageType)
	{
		this.selectedPageType = Objects.requireNonNull(selectedPageType);
	}
	
	public PageOrientation getSelectedPageOrientation()
	{
		return this.selectedPageOrientation;
	}
	
	public void setSelectedPageOrientation(final PageOrientation selectedPageOrientation)
	{
		this.selectedPageOrientation = Objects.requireNonNull(selectedPageOrientation);
	}
	
	public boolean isUsePageNumbering()
	{
		return this.usePageNumbering;
	}
	
	public void setUsePageNumbering(final boolean usePageNumbering)
	{
		this.usePageNumbering = usePageNumbering;
	}
	
	public int getPageMargin()
	{
		return this.pageMargin;
	}
	
	public void setPageMargin(final int pageMargin)
	{
		if(pageMargin < PAGE_MARGIN_MIN || pageMargin > PAGE_MARGIN_MAX)
		{
			throw new IllegalArgumentException("Invalid pageMargin value");
		}
		this.pageMargin = pageMargin;
	}
}
