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
package software.xdev.vaadin.grid_exporter.grid;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.grid.ColumnPathRenderer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.function.ValueProvider;

import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;


/**
 * Extracts the sorted and filtered data from a {@link Grid}.
 * <p>
 * Be aware that this class uses reflection to achieve it's goals.
 * </p>
 */
@SuppressWarnings({"java:S3011", "unchecked"}) // Accessing non-public Vaadin fields
public class GridDataExtractor<T>
{
	protected final Grid<T> grid;
	
	public GridDataExtractor(final Grid<T> grid)
	{
		this.grid = Objects.requireNonNull(grid);
	}
	
	public List<List<String>> getSortedAndFilteredData(final List<ColumnConfiguration<T>> columnsToExport)
	{
		return this.getSortedAndFilteredData(this.grid)
			.map(item -> columnsToExport.stream()
				.map(column -> this.getFormattedValue(column.getGridColumn(), item))
				.collect(Collectors.toList()))
			.collect(Collectors.toList());
	}
	
	protected String getFormattedValue(final Grid.Column<T> column, final T item)
	{
		try
		{
			final Renderer<T> renderer = column.getRenderer();
			final ValueProvider<T, ?> valueProvider = this.getValueProvider(column);
			if(valueProvider != null)
			{
				final Method getValueFormatter = this.getValueFormatter(renderer);
				final Object value = valueProvider.apply(item);
				if(value != null && getValueFormatter != null)
				{
					return (String)getValueFormatter.invoke(renderer, value);
				}
			}
			else if(renderer instanceof TextRenderer)
			{
				final Field itemLabelGenerator = TextRenderer.class.getDeclaredField("itemLabelGenerator");
				itemLabelGenerator.setAccessible(true);
				return ((ItemLabelGenerator<T>)itemLabelGenerator.get(renderer)).apply(item);
			}
			else if(renderer instanceof ColumnPathRenderer)
			{
				for(final ValueProvider<T, ?> valprov : renderer.getValueProviders().values())
				{
					if(valprov != null)
					{
						return valprov.apply(item).toString();
					}
				}
			}
		}
		catch(final IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchFieldException | SecurityException e)
		{
			// Something went wrong, but it's not our place to say what or why.
		}
		return null;
	}
	
	protected <R> Method getValueFormatter(final R renderer)
	{
		for(final Method m : renderer.getClass().getDeclaredMethods())
		{
			if(m.getName().contentEquals("getFormattedValue"))
			{
				m.setAccessible(true);
				return m;
			}
		}
		return null;
	}
	
	protected <X> ValueProvider<T, X> getValueProvider(final Grid.Column<T> column)
	{
		final Renderer<T> r = column.getRenderer();
		if(r instanceof BasicRenderer)
		{
			try
			{
				final Method getValueProvider = BasicRenderer.class.getDeclaredMethod("getValueProvider");
				getValueProvider.setAccessible(true);
				return (ValueProvider<T, X>)getValueProvider.invoke(r);
			}
			catch(final IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e)
			{
				// Something went wrong, but it's not our place to say what or why.
			}
		}
		return null;
	}
	
	protected Stream<T> getSortedAndFilteredData(final Grid<T> grid)
	{
		return grid.getDataProvider().fetch(
			new Query<>(
				0,
				Integer.MAX_VALUE,
				grid.getSortOrder().stream()
					.flatMap(so -> so.getSorted().getSortOrder(so.getDirection()))
					.collect(Collectors.toList()),
				grid.getDataCommunicator().getInMemorySorting(),
				null));
	}
}
