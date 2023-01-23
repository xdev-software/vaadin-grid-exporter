package software.xdev.vaadin.grid_exporter.format;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.Grid;

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
	
	private final Grid<T> grid;
	
	private final List<ColumnConfiguration<T>> columnConfigurations;
	private String title = "";                        // No text = no title
	
	public GeneralConfig(final Grid<T> grid)
	{
		this(grid, null, null);
	}
	
	public GeneralConfig(final Grid<T> grid, final Predicate<Grid.Column<T>> columnFilter)
	{
		this(grid, columnFilter, null);
	}
	
	public GeneralConfig(
		final Grid<T> grid,
		Predicate<Grid.Column<T>> columnFilter,
		ColumnConfigurationBuilder configurationBuilder)
	{
		this.grid = grid;
		
		if(columnFilter == null)
		{
			columnFilter = DefaultColumnFilter();
		}
		if(configurationBuilder == null)
		{
			configurationBuilder = getDefaultColumnConfigBuilder();
		}
		
		// This has to be done here so that it's already available for the ExportDialog
		this.columnConfigurations = this.grid.getColumns().stream()
			.filter(columnFilter)
			.map(configurationBuilder::build)
			.collect(Collectors.toList());
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
	
}
