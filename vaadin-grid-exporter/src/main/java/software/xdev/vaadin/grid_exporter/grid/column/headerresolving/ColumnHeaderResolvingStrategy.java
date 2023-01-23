/*
 * Copyright © 2022 XDEV Software (https://xdev.software/en)
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
package software.xdev.vaadin.grid_exporter.grid.column.headerresolving;



import java.util.Optional;

import com.vaadin.flow.component.grid.Grid.Column;


/**
 * Can be implemented to create a new strategy that resolves a {@link Column} header
 */
public interface ColumnHeaderResolvingStrategy
{
	/**
	 * Resolves the text for a column header
	 *
	 * @param column
	 *            The column for which the header text should be resolved
	 * @return
	 *         {@link Optional#empty()} when the text could not be resolved and the next function should be used.<br/>
	 *         If a value was found the Optional contains the string.
	 */
	Optional<String> resolve(Column<?> column);
}
