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
package software.xdev.vaadin.grid_exporter.jasper.config.title;

import java.util.Objects;

import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class TitleConfig implements SpecificConfig
{
	protected String title;
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle(final String title)
	{
		this.title = Objects.requireNonNull(title);
	}
	
	public boolean notTitleEmpty()
	{
		return this.getTitle() != null && !this.getTitle().isBlank();
	}
}
