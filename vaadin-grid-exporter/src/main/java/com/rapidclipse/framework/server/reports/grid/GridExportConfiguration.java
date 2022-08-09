package com.rapidclipse.framework.server.reports.grid;

/*-
 * #%L
 * vaadin-grid-exporter
 * %%
 * Copyright (C) 2022 XDEV Software
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.awt.Insets;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.rapidclipse.framework.server.reports.Format;
import com.rapidclipse.framework.server.reports.grid.column.ColumnConfiguration;
import com.rapidclipse.framework.server.reports.grid.column.ColumnConfigurationBuilder;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;

import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;


public class GridExportConfiguration<T>
{
	public static <T> Predicate<Column<T>> DefaultColumnFilter()
	{
		return col -> true;
	}
	
	public static ColumnConfigurationBuilder getDefaultColumnConfigBuilder()
	{
		return new ColumnConfigurationBuilder()
			.withColumnConfigHeaderResolvingStrategyBuilder(headerResolvingStrategyBuilder ->
			{
				headerResolvingStrategyBuilder
					.withVaadinInternalHeaderStrategy();
			});
	}
	
	private final Grid<T> grid;
	
	private final List<ColumnConfiguration<T>> columnConfigurations;
	private Format[] availableFormats = Format.All();
	private Format format = Format.Pdf();
	private String title = "";                        // No text = no title
	private PageType pageType = PageType.A4;
	private PageOrientation pageOrientation = PageOrientation.PORTRAIT;
	private Insets pageMargin = new Insets(20, 20, 20, 20);
	private boolean showPageNumber = false;
	private boolean highlightRows = false;
	
	public GridExportConfiguration(final Grid<T> grid)
	{
		this(grid, null, null);
	}
	
	public GridExportConfiguration(final Grid<T> grid, final Predicate<Column<T>> columnFilter)
	{
		this(grid, columnFilter, null);
	}
	
	public GridExportConfiguration(
		final Grid<T> grid,
		Predicate<Column<T>> columnFilter,
		ColumnConfigurationBuilder configurationBuilder)
	{
		this.grid = grid;
		
		if(columnFilter == null)
		{
			columnFilter = DefaultColumnFilter();
		}
		if(configurationBuilder == null)
		{
			configurationBuilder = getDefaultColumnConfigBuilder();
		}
		
		// This has to be done here so that it's already available for the ExportDialog
		this.columnConfigurations = this.grid.getColumns().stream()
			.filter(columnFilter)
			.map(configurationBuilder::build)
			.collect(Collectors.toList());
	}
	
	/**
	 * @return the columnConfigurations
	 */
	public List<ColumnConfiguration<T>> getColumnConfigurations()
	{
		return this.columnConfigurations;
	}
	
	public Grid<T> getGrid()
	{
		return this.grid;
	}
	
	public GridExportConfiguration<T> setAvailableFormats(final Format... availableFormats)
	{
		this.availableFormats = availableFormats;
		return this;
	}
	
	public Format[] getAvailableFormats()
	{
		return this.availableFormats;
	}
	
	public GridExportConfiguration<T> setFormat(final Format format)
	{
		this.format = format;
		return this;
	}
	
	public Format getFormat()
	{
		return this.format;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public GridExportConfiguration<T> setTitle(final String title)
	{
		this.title = title;
		return this;
	}
	
	public PageType getPageType()
	{
		return this.pageType;
	}
	
	public GridExportConfiguration<T> setPageType(final PageType pageType)
	{
		this.pageType = pageType;
		return this;
	}
	
	public PageOrientation getPageOrientation()
	{
		return this.pageOrientation;
	}
	
	public GridExportConfiguration<T> setPageOrientation(final PageOrientation pageOrientation)
	{
		this.pageOrientation = pageOrientation;
		return this;
	}
	
	public Insets getPageMargin()
	{
		return this.pageMargin;
	}
	
	public GridExportConfiguration<T> setPageMargin(final Insets pageMargin)
	{
		this.pageMargin = pageMargin;
		return this;
	}
	
	public boolean isShowPageNumber()
	{
		return this.showPageNumber;
	}
	
	public GridExportConfiguration<T> setShowPageNumber(final boolean showPageNumber)
	{
		this.showPageNumber = showPageNumber;
		return this;
	}
	
	public boolean isHighlightRows()
	{
		return this.highlightRows;
	}
	
	public GridExportConfiguration<T> setHighlightRows(final boolean highlightRows)
	{
		this.highlightRows = highlightRows;
		return this;
	}
}
