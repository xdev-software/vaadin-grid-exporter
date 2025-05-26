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
package software.xdev.vaadin.grid_exporter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import com.vaadin.flow.component.grid.Grid;

import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.column.ColumnConfigurationBuilder;
import software.xdev.vaadin.grid_exporter.column.ColumnConfigurationHeaderResolvingStrategyBuilder;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.grid.GridDataExtractor;
import software.xdev.vaadin.grid_exporter.jasper.JasperGridExporterProvider;
import software.xdev.vaadin.grid_exporter.wizard.GridExporterWizard;
import software.xdev.vaadin.grid_exporter.wizard.GridExporterWizardState;


/**
 * Opens a wizard to export a Grid.
 *
 * @param <T> The grid bean type
 */
public class GridExporter<T>
{
	protected final Grid<T> grid;
	
	protected GridExportLocalizationConfig localizationConfig = new GridExportLocalizationConfig();
	
	protected Predicate<Grid.Column<T>> columnFilter = col -> true;
	
	protected ColumnConfigurationBuilder columnConfigurationBuilder = new ColumnConfigurationBuilder()
		.withColumnConfigHeaderResolvingStrategyBuilder(
			ColumnConfigurationHeaderResolvingStrategyBuilder::withVaadinInternalHeaderStrategy);
	
	protected String fileName = "Report_" + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm")
		.format(LocalDateTime.now(ZoneOffset.UTC));
	
	protected List<Format> availableFormats = new ArrayList<>();
	
	protected Format preSelectedFormat;
	
	protected Function<Grid<T>, GridDataExtractor<T>> gridDataExtractorSupplier = GridDataExtractor::new;
	
	public GridExporter(final Grid<T> grid)
	{
		this.grid = Objects.requireNonNull(grid);
	}
	
	public GridExporter<T> loadFromProvider(final GridExporterProvider provider)
	{
		this.localizationConfig.withAll(provider.getDefaultTranslationKeyValues());
		this.availableFormats.addAll(provider.getFormats());
		return this;
	}
	
	public GridExporter<T> withLocalizationConfig(final GridExportLocalizationConfig localizationConfig)
	{
		this.localizationConfig = localizationConfig;
		return this;
	}
	
	public GridExporter<T> withColumnFilter(final Predicate<Grid.Column<T>> columnFilter)
	{
		this.columnFilter = columnFilter;
		return this;
	}
	
	public GridExporter<T> withColumnConfigurationBuilder(final ColumnConfigurationBuilder columnConfigurationBuilder)
	{
		this.columnConfigurationBuilder = columnConfigurationBuilder;
		return this;
	}
	
	public GridExporter<T> withFileName(final String fileName)
	{
		this.fileName = Objects.requireNonNull(fileName);
		return this;
	}
	
	public GridExporter<T> withAvailableFormats(final Format... availableFormats)
	{
		return this.withAvailableFormats(Arrays.asList(availableFormats));
	}
	
	public GridExporter<T> withAvailableFormats(final List<Format> availableFormats)
	{
		Objects.requireNonNull(availableFormats);
		if(availableFormats.isEmpty())
		{
			throw new IllegalStateException("Available formats is empty");
		}
		
		this.availableFormats = new ArrayList<>(availableFormats);
		return this;
	}
	
	public GridExporter<T> addAvailableFormat(final Format availableFormat)
	{
		Objects.requireNonNull(availableFormat);
		this.availableFormats.add(availableFormat);
		return this;
	}
	
	public GridExporter<T> withPreSelectedFormat(final Format preSelectedFormat)
	{
		Objects.requireNonNull(preSelectedFormat);
		if(!this.availableFormats.contains(preSelectedFormat))
		{
			throw new IllegalArgumentException("Available formats must contain Preselected format");
		}
		this.preSelectedFormat = preSelectedFormat;
		return this;
	}
	
	public GridExporter<T> withGridDataExtractorSupplier(
		final Function<Grid<T>, GridDataExtractor<T>> dataExtractorSupplier)
	{
		this.gridDataExtractorSupplier = Objects.requireNonNull(dataExtractorSupplier);
		return this;
	}
	
	protected List<ColumnConfiguration<T>> generateAvailableColumns()
	{
		return this.grid.getColumns().stream()
			.filter(this.columnFilter)
			.map(this.columnConfigurationBuilder::build)
			.toList();
	}
	
	/**
	 * Opens the {@link GridExporterWizard}.
	 */
	public void open()
	{
		final GridExporterWizardState<T> state = new GridExporterWizardState<>(
			this.gridDataExtractorSupplier.apply(this.grid),
			this.availableFormats,
			this.generateAvailableColumns());
		state.setFileName(this.fileName);
		
		if(this.preSelectedFormat == null)
		{
			this.preSelectedFormat = this.availableFormats.get(0);
		}
		state.setSelectedFormat(this.preSelectedFormat);
		
		new GridExporterWizard<>(state, this.localizationConfig).open();
	}
	
	/**
	 * Creates a new {@link GridExporter} with the default JasperReports exports.
	 * <p>
	 * <i>Note: This may be removed or split into a separate module in the future.</i>
	 * </p>
	 */
	public static <T> GridExporter<T> newWithDefaults(final Grid<T> grid)
	{
		return new GridExporter<>(grid)
			.loadFromProvider(new JasperGridExporterProvider());
	}
}
