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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class DelegatingDataSource implements JRDataSource
{
	private final JRDataSource delegate;
	
	public DelegatingDataSource(final JRDataSource delegate)
	{
		this.delegate = delegate;
	}
	
	@Override
	public boolean next() throws JRException
	{
		return this.delegate.next();
	}
	
	@Override
	public Object getFieldValue(final JRField jrField) throws JRException
	{
		return this.delegate.getFieldValue(jrField);
	}
}
