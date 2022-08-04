package com.rapidclipse.framework.server.reports.grid.column;

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

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.function.SerializableFunction;


public class ColumnConfiguration<T>
{
	private final Column<T> gridColumn;
	private boolean         visible = true;
	private String          header;
	private ColumnTextAlign columnAlignment;
	private Integer         columnWidth;
	
	ColumnConfiguration(
		final Column<T> gridColumn,
		final SerializableFunction<Column<?>, String> headerResolver)
	{
		this.gridColumn      = gridColumn;
		this.header          = headerResolver.apply(gridColumn);
		this.columnAlignment = gridColumn.getTextAlign();
	}
	
	public Column<T> getGridColumn()
	{
		return this.gridColumn;
	}
	
	public boolean isVisible()
	{
		return this.visible;
	}
	
	public ColumnConfiguration<T> setVisible(final boolean visible)
	{
		this.visible = visible;
		return this;
	}
	
	public String getHeader()
	{
		return this.header;
	}
	
	public ColumnConfiguration<T> setHeader(final String header)
	{
		this.header = header;
		return this;
	}
	
	public ColumnTextAlign getColumnAlignment()
	{
		return this.columnAlignment;
	}
	
	public ColumnConfiguration<T> setColumnAlignment(final ColumnTextAlign columnAlignment)
	{
		this.columnAlignment = columnAlignment;
		return this;
	}
	
	public Integer getColumnWidth()
	{
		return this.columnWidth;
	}
	
	public ColumnConfiguration<T> setColumnWidth(final Integer columnWidth)
	{
		this.columnWidth = columnWidth;
		return this;
	}
}
