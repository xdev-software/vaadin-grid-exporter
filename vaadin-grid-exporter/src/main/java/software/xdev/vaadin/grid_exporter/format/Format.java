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

import java.util.List;
import java.util.function.Function;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.grid.GridDataExtractor;


/**
 * Defines a format to export grid data to.
 */
public interface Format
{
	String getFormatNameToDisplay();
	
	String getFormatFilenameSuffix();
	
	String getMimeType();
	
	// Either ignore a rawtype or a generic wildcard type warning
	@SuppressWarnings("java:S1452")
	List<Function<Translator, ? extends SpecificConfigComponent<? extends SpecificConfig>>> getConfigComponents();
	
	<T> byte[] export(
		GridDataExtractor<T> gridDataExtractor,
		List<ColumnConfiguration<T>> columnsToExport,
		List<? extends SpecificConfig> configs);
}
