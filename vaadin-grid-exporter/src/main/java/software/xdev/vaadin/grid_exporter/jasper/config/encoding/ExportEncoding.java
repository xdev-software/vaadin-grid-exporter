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
package software.xdev.vaadin.grid_exporter.jasper.config.encoding;

import java.nio.charset.Charset;


public class ExportEncoding
{
	protected final Charset charset;
	protected final String bom;
	
	public ExportEncoding(final Charset charset)
	{
		this(charset, null);
	}
	
	public ExportEncoding(final Charset charset, final String bom)
	{
		this.charset = charset;
		this.bom = bom;
	}
	
	public Charset charset()
	{
		return this.charset;
	}
	
	public String bom()
	{
		return this.bom;
	}
	
	public boolean hasBom()
	{
		return this.bom() != null;
	}
}
