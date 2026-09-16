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
package software.xdev.vaadin.grid_exporter.jasper.config.header;

import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class HeaderConfig implements SpecificConfig
{
	protected boolean exportHeader;
	
	public HeaderConfig()
	{
		this(true);
	}
	
	public HeaderConfig(final boolean exportHeader)
	{
		this.exportHeader = exportHeader;
	}
	
	public boolean isExportHeader()
	{
		return this.exportHeader;
	}
	
	public void setExportHeader(final boolean exportHeader)
	{
		this.exportHeader = exportHeader;
	}
}
