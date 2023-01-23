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
package software.xdev.vaadin.grid_exporter.grid.column;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.function.SerializableFunction;

import software.xdev.vaadin.grid_exporter.grid.column.headerresolving.ColumnHeaderResolvingStrategy;
import software.xdev.vaadin.grid_exporter.grid.column.headerresolving.ManualColumnHeaderResolvingStrategy;
import software.xdev.vaadin.grid_exporter.grid.column.headerresolving.VaadinInternalRenderingColumnHeaderResolvingStrategy;


/**
 * Builds a function (from multiple strategies) that resolves the text for a column.<br/> If no strategies are specified
 * the fallback {@link Column#getKey()} is used.
 */
public class ColumnConfigurationHeaderResolvingStrategyBuilder
{
	private final List<ColumnHeaderResolvingStrategy> strategies = new ArrayList<>();
	
	/**
	 * Uses the {@link VaadinInternalRenderingColumnHeaderResolvingStrategy}
	 */
	public ColumnConfigurationHeaderResolvingStrategyBuilder withVaadinInternalHeaderStrategy()
	{
		return this.withStrategy(new VaadinInternalRenderingColumnHeaderResolvingStrategy());
	}
	
	/**
	 * Uses the {@link ManualColumnHeaderResolvingStrategy}
	 */
	public <I> ColumnConfigurationHeaderResolvingStrategyBuilder withManualColumnHeaderStrategy(
		final Function<Column<?>, I> identifierResolver,
		final Map<I, Function<I, String>> headerTextResolverMap)
	{
		return this.withStrategy(new ManualColumnHeaderResolvingStrategy<>(identifierResolver, headerTextResolverMap));
	}
	
	/**
	 * Adds a new {@link ColumnConfigHeaderResolvingStrategy}.<br/>
	 * This strategy will be added at the end of the strategy list.
	 */
	public ColumnConfigurationHeaderResolvingStrategyBuilder withStrategy(final ColumnHeaderResolvingStrategy strategy)
	{
		this.strategies.add(strategy);
		return this;
	}
	
	/**
	 * Adds a new {@link ColumnConfigHeaderResolvingStrategy}.<br/>
	 * This strategy will be added at the start of the strategy list.
	 */
	public ColumnConfigurationHeaderResolvingStrategyBuilder
		withFirstStrategy(final ColumnHeaderResolvingStrategy strategy)
	{
		this.strategies.add(0, strategy);
		return this;
	}
	
	/**
	 * Clears all existing strategies
	 */
	public ColumnConfigurationHeaderResolvingStrategyBuilder clearAllStrategies()
	{
		this.strategies.clear();
		return this;
	}
	
	public SerializableFunction<Column<?>, String> build()
	{
		return col ->
		{
			for(final ColumnHeaderResolvingStrategy resolvingFunction : this.strategies)
			{
				final Optional<String> optResolvedValue = resolvingFunction.resolve(col);
				if(optResolvedValue.isPresent())
				{
					return optResolvedValue.get();
				}
			}
			
			// Fallback
			if(col.getKey() != null)
			{
				return col.getKey();
			}
			return "";
		};
	}
}
