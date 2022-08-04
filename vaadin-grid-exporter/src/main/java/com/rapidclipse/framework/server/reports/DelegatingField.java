package com.rapidclipse.framework.server.reports;

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

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;


public abstract class DelegatingField implements JRField
{
	private final JRField delegate;
	
	protected DelegatingField(final JRField delegate)
	{
		this.delegate = delegate;
	}
	
	@Override
	public String getDescription()
	{
		return this.delegate.getDescription();
	}
	
	@Override
	public String getName()
	{
		return this.delegate.getName();
	}
	
	@Override
	public Class<?> getValueClass()
	{
		return this.delegate.getValueClass();
	}
	
	@Override
	public String getValueClassName()
	{
		return this.delegate.getValueClassName();
	}
	
	@Override
	public void setDescription(final String arg0)
	{
		this.delegate.setDescription(arg0);
	}
	
	@Override
	public JRPropertiesHolder getParentProperties()
	{
		return this.delegate.getParentProperties();
	}
	
	@Override
	public JRPropertiesMap getPropertiesMap()
	{
		return this.delegate.getPropertiesMap();
	}
	
	@Override
	public boolean hasProperties()
	{
		return this.delegate.hasProperties();
	}
	
	@Override
	public JRPropertyExpression[] getPropertyExpressions()
	{
		return this.delegate.getPropertyExpressions();
	}
	
	@Override
	public Object clone()
	{
		return this.delegate.clone();
	}
}
