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
package software.xdev.vaadin.grid_exporter.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.components.wizard.WizardState;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.grid.GridDataExtractor;


public class GridExporterWizardState<T> implements WizardState
{
	protected final GridDataExtractor<T> gridDataExtractor;
	protected final List<Format> availableFormats;
	protected final List<ColumnConfiguration<T>> availableColumns;
	
	protected String fileName = "";
	protected List<ColumnConfiguration<T>> selectedColumns;
	protected Format selectedFormat = null;
	
	protected final List<SpecificConfig> specificConfigs = new ArrayList<>();
	
	public GridExporterWizardState(
		final GridDataExtractor<T> gridDataExtractor,
		final List<Format> availableFormats,
		final List<ColumnConfiguration<T>> availableColumns)
	{
		this.gridDataExtractor = Objects.requireNonNull(gridDataExtractor);
		this.availableFormats = Objects.requireNonNull(availableFormats);
		this.availableColumns = Objects.requireNonNull(availableColumns);
		
		// By default, all columns should be selected
		this.selectedColumns = new ArrayList<>(availableColumns);
	}
	
	public GridDataExtractor<T> getGridDataExtractor()
	{
		return this.gridDataExtractor;
	}
	
	public List<Format> getAvailableFormats()
	{
		return this.availableFormats;
	}
	
	public List<ColumnConfiguration<T>> getAvailableColumns()
	{
		return this.availableColumns;
	}
	
	public List<ColumnConfiguration<T>> getSelectedColumns()
	{
		return this.selectedColumns;
	}
	
	public String getFileName()
	{
		return this.fileName;
	}
	
	public void setFileName(final String fileName)
	{
		this.fileName = Objects.requireNonNull(fileName);
	}
	
	public void setSelectedColumns(final List<ColumnConfiguration<T>> selectedColumns)
	{
		this.selectedColumns = Objects.requireNonNull(selectedColumns);
	}
	
	public Format getSelectedFormat()
	{
		return this.selectedFormat;
	}
	
	public void setSelectedFormat(final Format selectedFormat)
	{
		this.selectedFormat = selectedFormat;
	}
	
	public List<SpecificConfig> getSpecificConfigs()
	{
		return this.specificConfigs;
	}
}
