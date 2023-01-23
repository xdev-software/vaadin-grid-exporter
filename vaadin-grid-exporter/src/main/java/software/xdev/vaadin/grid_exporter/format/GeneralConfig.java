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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.Grid;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.jasper.pdf.PdfFormat;
import software.xdev.vaadin.grid_exporter.grid.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.grid.column.ColumnConfigurationBuilder;


public class GeneralConfig<T>
{
	public static <T> Predicate<Grid.Column<T>> DefaultColumnFilter()
	{
		return col -> true;
	}
	
	public static ColumnConfigurationBuilder getDefaultColumnConfigBuilder()
	{
		return new ColumnConfigurationBuilder()
			.withColumnConfigHeaderResolvingStrategyBuilder(headerResolvingStrategyBuilder ->
			{
				headerResolvingStrategyBuilder
					.withVaadinInternalHeaderStrategy();
			});
	}
	
	private final List<ColumnConfiguration<T>> columnConfigurations;
	private String title = "";                        // No text = no title
	private final String fileName = "ExportedGrid";
	
	private final Translator translator;
	
	private final List<software.xdev.vaadin.grid_exporter.format.Format<T, ?>> availableFormats;
	private Format<T, ?> preselectedFormat;
	
	public GeneralConfig(final Grid<T> grid)
	{
		this(grid, null, null);
	}
	
	public GeneralConfig(final Grid<T> grid, final Translator translator)
	{
		this(grid, null, translator);
	}
	
	public GeneralConfig(final Grid<T> grid, final Predicate<Grid.Column<T>> columnFilter)
	{
		this(grid, columnFilter, null, null);
	}
	
	public GeneralConfig(final Grid<T> grid, final Predicate<Grid.Column<T>> columnFilter, final Translator translator)
	{
		this(grid, columnFilter, null, translator);
	}
	
	public GeneralConfig(
		final Grid<T> grid,
		Predicate<Grid.Column<T>> columnFilter,
		ColumnConfigurationBuilder configurationBuilder,
		final Translator translator)
	{
		this.translator = translator;
		if(columnFilter == null)
		{
			columnFilter = DefaultColumnFilter();
		}
		if(configurationBuilder == null)
		{
			configurationBuilder = getDefaultColumnConfigBuilder();
		}
		
		this.preselectedFormat = new PdfFormat<>(translator);
		this.availableFormats = Arrays.asList(this.preselectedFormat);
		
		// This has to be done here so that it's already available for the ExportDialog
		this.columnConfigurations = grid.getColumns().stream()
			.filter(columnFilter)
			.map(configurationBuilder::build)
			.collect(Collectors.toList());
	}
	
	public <E extends SpecificConfig> GeneralConfig<T> withFormat(final Format<T, E> format)
	{
		this.availableFormats.add(format);
		return this;
	}
	
	public <E extends SpecificConfig> GeneralConfig<T> withPreselectedFormat(final Format<T, E> format)
	{
		if(!this.availableFormats.contains(format))
		{
			throw new RuntimeException(
				"Can't select unavailable format. Please make shure the format is available in 'availableFormats'.");
		}
		this.preselectedFormat = format;
		return this;
	}
	
	/**
	 * @return the columnConfigurations
	 */
	public List<ColumnConfiguration<T>> getColumnConfigurations()
	{
		return this.columnConfigurations;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public GeneralConfig<T> withTitle(final String title)
	{
		this.title = title;
		return this;
	}
	
	public String getFileName()
	{
		return this.fileName;
	}
	
	public List<Format<T, ?>> getAvailableFormats()
	{
		return this.availableFormats;
	}
	
	public Format<T, ?> getPreselectedFormat()
	{
		return this.preselectedFormat;
	}
}
