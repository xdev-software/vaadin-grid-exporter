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
import software.xdev.vaadin.grid_exporter.format.jasper.CsvFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.DocxFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.HtmlFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.OdsFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.OdtFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.PptxFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.RtfFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.TextFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.XlsFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.XlsxFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.XmlFormat;
import software.xdev.vaadin.grid_exporter.format.jasper.pdf.PdfFormat;
import software.xdev.vaadin.grid_exporter.grid.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.grid.column.ColumnConfigurationBuilder;


/**
 * Configurations that are valid for every {@link Format}. The configuration of every available column is contained
 * here.
 *
 * @param <T> type of the grid elements
 */
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
	private final String fileName = "ExportedGrid";
	private List<software.xdev.vaadin.grid_exporter.format.Format<T, ?>> availableFormats;
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
		if(columnFilter == null)
		{
			columnFilter = DefaultColumnFilter();
		}
		if(configurationBuilder == null)
		{
			configurationBuilder = getDefaultColumnConfigBuilder();
		}
		
		this.availableFormats = this.createAvailableFormats(translator);
		
		// This has to be done here so that it's already available for the ExportDialog
		this.columnConfigurations = grid.getColumns().stream()
			.filter(columnFilter)
			.map(configurationBuilder::build)
			.collect(Collectors.toList());
	}
	
	protected List<software.xdev.vaadin.grid_exporter.format.Format<T, ?>> createAvailableFormats(final Translator translator)
	{
		this.preselectedFormat = new PdfFormat<>(translator);
		return Arrays.asList(
			new CsvFormat<>(translator),
			new DocxFormat<>(translator),
			new HtmlFormat<>(translator),
			new OdsFormat<>(translator),
			new OdtFormat<>(translator),
			new PptxFormat<>(translator),
			this.preselectedFormat,
			new RtfFormat<>(translator),
			new TextFormat<>(translator),
			new XlsFormat<>(translator),
			new XlsxFormat<>(translator),
			new XmlFormat<>(translator)
		);
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
			throw new IllegalStateException(
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
	
	public String getFileName()
	{
		return this.fileName;
	}
	
	public List<Format<T, ?>> getAvailableFormats()
	{
		return this.availableFormats;
	}
	
	public void setAvailableFormats(final List<Format<T, ?>> availableFormats)
	{
		this.availableFormats = availableFormats;
	}
	
	public Format<T, ?> getPreselectedFormat()
	{
		return this.preselectedFormat;
	}
}
