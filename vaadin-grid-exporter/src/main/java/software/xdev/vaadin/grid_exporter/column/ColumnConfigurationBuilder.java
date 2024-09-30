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



import java.util.function.Consumer;

import com.vaadin.flow.component.grid.Grid.Column;


/**
 * Used for (initially) building a {@link ColumnConfiguration}
 */
public class ColumnConfigurationBuilder
{
	protected final ColumnConfigurationHeaderResolvingStrategyBuilder columnConfigHeaderResolvingStrategyBuilder =
		new ColumnConfigurationHeaderResolvingStrategyBuilder();
	
	public ColumnConfigurationBuilder withColumnConfigHeaderResolvingStrategyBuilder(
		final Consumer<ColumnConfigurationHeaderResolvingStrategyBuilder> configureBuilderFunc)
	{
		configureBuilderFunc.accept(this.columnConfigHeaderResolvingStrategyBuilder);
		return this;
	}
	
	public <T> ColumnConfiguration<T> build(final Column<T> gridColumn)
	{
		return new ColumnConfiguration<>(gridColumn, this.columnConfigHeaderResolvingStrategyBuilder.build());
	}
}
