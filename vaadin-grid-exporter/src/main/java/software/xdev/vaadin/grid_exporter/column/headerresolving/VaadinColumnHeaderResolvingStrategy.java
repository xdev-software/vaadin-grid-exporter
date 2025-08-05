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

import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.dom.Element;


/**
 * Tries to get the header using Vaadin Column API methods
 * <p/>
 * Note: This might fail when the vaadin version changes
 */
public class VaadinColumnHeaderResolvingStrategy implements ColumnHeaderResolvingStrategy
{
	@Override
	public Optional<String> resolve(final Column<?> column)
	{
		try
		{
			final Optional<String> optHeaderComponentText = Optional.ofNullable(column.getHeaderComponent())
				.map(Component::getElement)
				.map(Element::getText);
			if(optHeaderComponentText.isPresent())
			{
				return optHeaderComponentText;
			}
			
			return Optional.ofNullable(column.getHeaderText());
		}
		catch(final Exception e)
		{
			return Optional.empty();
		}
	}
	
}
