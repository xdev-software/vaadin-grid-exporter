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
package software.xdev.vaadin.grid_exporter.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import software.xdev.vaadin.grid_exporter.Translator;


public abstract class AbstractFormat implements Format
{
	protected final String nameToDisplay;
	
	protected final String fileSuffix;
	
	protected final String mimeType;
	
	protected List<Function<Translator, ? extends SpecificConfigComponent<? extends SpecificConfig>>> configComponents =
		new ArrayList<>();
	
	protected AbstractFormat(
		final String nameToDisplay,
		final String fileSuffix,
		final String mimeType)
	{
		this.nameToDisplay = Objects.requireNonNull(nameToDisplay);
		this.fileSuffix = Objects.requireNonNull(fileSuffix);
		this.mimeType = Objects.requireNonNull(mimeType);
	}
	
	@SafeVarargs
	public final void withConfigComponents(
		final Function<Translator, ? extends SpecificConfigComponent<? extends SpecificConfig>>... configComponents)
	{
		this.configComponents.addAll(Arrays.asList(configComponents));
	}
	
	@Override
	public String getFormatNameToDisplay()
	{
		return this.nameToDisplay;
	}
	
	@Override
	public String getFormatFilenameSuffix()
	{
		return this.fileSuffix;
	}
	
	@Override
	public String getMimeType()
	{
		return this.mimeType;
	}
	
	@Override
	public List<Function<Translator, ? extends SpecificConfigComponent<? extends SpecificConfig>>> getConfigComponents()
	{
		return this.configComponents;
	}
}
