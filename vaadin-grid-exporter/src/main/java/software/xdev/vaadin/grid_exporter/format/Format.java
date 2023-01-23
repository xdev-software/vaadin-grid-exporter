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

import com.vaadin.flow.component.grid.Grid;


public interface Format<T, E extends SpecificConfig>
{
	String getFormatNameToDisplay();
	
	String getFormatFilenameSuffix();
	
	String getMimeType();
	
	/**
	 * Returns if a preview can be shown in a "standardized" browser (e.g. the latest versions of Chrome and Firefox)
	 *
	 * @return <code>true</code> if a preview can be shown in the browser
	 */
	boolean isPreviewableInStandardBrowser();
	
	FormatConfigComponent<E> createConfigurationComponent();
	
	byte[] export(Grid<T> gridToExport, GeneralConfig<T> generalConfig, E specificConfig);
}
