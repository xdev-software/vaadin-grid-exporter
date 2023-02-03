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
package software.xdev.vaadin.grid_exporter.column;

import java.util.Objects;

import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.function.SerializableFunction;


public class ColumnConfiguration<T>
{
	private final Column<T> gridColumn;
	private String header;
	
	ColumnConfiguration(
		final Column<T> gridColumn,
		final SerializableFunction<Column<?>, String> headerResolver)
	{
		this.gridColumn = gridColumn;
		this.header = headerResolver.apply(gridColumn);
	}
	
	public Column<T> getGridColumn()
	{
		return this.gridColumn;
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
	
	public String getKeyOrHeader()
	{
		return this.getGridColumn().getKey() != null
			? this.getGridColumn().getKey()
			: this.getHeader();
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if(this == o)
		{
			return true;
		}
		if(!(o instanceof ColumnConfiguration))
		{
			return false;
		}
		final ColumnConfiguration<?> that = (ColumnConfiguration<?>)o;
		return Objects.equals(this.gridColumn, that.gridColumn) && Objects.equals(this.header, that.header);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(this.gridColumn, this.header);
	}
}
