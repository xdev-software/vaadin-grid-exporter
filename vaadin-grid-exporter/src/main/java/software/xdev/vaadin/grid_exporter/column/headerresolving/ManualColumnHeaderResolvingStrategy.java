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
package software.xdev.vaadin.grid_exporter.column.headerresolving;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.vaadin.flow.component.grid.Grid.Column;


/**
 * Resolves the header-text by using the unique identification {@linkplain I} and find the corresponding resolving
 * function for the identification in a map.
 * <br>
 * This function is the used to resolve the header-text.
 * <p>
 * Example:
 * <br>
 * {@code new ManualColumnHeaderResolvingStrategy(col -> col.getKey(), Map.of("name", k -> "Username", "pw", k ->
 * "Password"))}
 * </p>
 *
 * @param <I> The identifier of the column, e.g. the key or the column itself
 */
public class ManualColumnHeaderResolvingStrategy<I> implements ColumnHeaderResolvingStrategy
{
	protected final Function<Column<?>, I> identifierResolver;
	protected final Map<I, Function<I, String>> headerTextResolvers;
	
	public ManualColumnHeaderResolvingStrategy(
		final Function<Column<?>, I> identifierResolver,
		final Map<I, Function<I, String>> headerTextResolvers)
	{
		this.identifierResolver = identifierResolver;
		this.headerTextResolvers = headerTextResolvers;
	}
	
	@Override
	public Optional<String> resolve(final Column<?> column)
	{
		final I identifier = this.identifierResolver.apply(column);
		
		final Function<I, String> resolveFunction = this.headerTextResolvers.get(identifier);
		if(resolveFunction == null)
		{
			return Optional.empty();
		}
		
		return Optional.ofNullable(resolveFunction.apply(identifier));
	}
	
}
