package com.rapidclipse.framework.server.reports.grid.column.headerresolving;

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

import java.lang.reflect.Field;
import java.util.Optional;

import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.renderer.Renderer;


/**
 * Tries to access vaadins internal fields of a Grid/Column to get the header.<br/>
 * This will resolve headers who have been set using {@link Column#setHeader(String)}.<br/>
 * <br/>
 * Note: This might fail when the vaadin version changes
 */
public class VaadinInternalRenderingColumnHeaderResolvingStrategy implements ColumnHeaderResolvingStrategy
{
	protected Field findFieldInClass(final Class<?> clazz, final String fieldName)
	{
		Class<?> currentClazz = clazz;
		do
		{
			try
			{
				return currentClazz.getDeclaredField(fieldName);
			}
			catch(final NoSuchFieldException e)
			{
				// continue search
			}
		}
		// stop when we got the field or reached top of class hierarchy
		// no need to search in Object as well
		while((currentClazz = currentClazz.getSuperclass()) != null && currentClazz != Object.class);
		
		return null;
	}
	
	@Override
	public Optional<String> resolve(final Column<?> column)
	{
		try
		{
			final Field colHeaderRendererField = this.findFieldInClass(Column.class, "headerRenderer");
			if(colHeaderRendererField == null)
			{
				return Optional.empty();
			}
			
			colHeaderRendererField.setAccessible(true);
			
			final Renderer<?> renderer      = (Renderer<?>)colHeaderRendererField.get(column);
			
			final Field       templateField = Renderer.class.getDeclaredField("template");
			templateField.setAccessible(true);
			
			return Optional.ofNullable((String)templateField.get(renderer));
		}
		catch(final Exception e)
		{
			return Optional.empty();
		}
	}
	
}
