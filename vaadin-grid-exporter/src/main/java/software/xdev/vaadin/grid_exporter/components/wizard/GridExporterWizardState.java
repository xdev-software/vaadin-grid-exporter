package software.xdev.vaadin.grid_exporter.components.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.vaadin.flow.component.grid.Grid;

import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;


public class GridExporterWizardState<T> implements WizardState
{
	protected final Grid<T> grid;
	protected final List<Format> availableFormats;
	protected final List<ColumnConfiguration<T>> availableColumns;
	
	protected String fileName = "";
	protected List<ColumnConfiguration<T>> selectedColumns;
	protected Format selectedFormat = null;
	
	protected final List<SpecificConfig> specificConfigs = new ArrayList<>();
	
	public GridExporterWizardState(
		final Grid<T> grid,
		final List<Format> availableFormats,
		final List<ColumnConfiguration<T>> availableColumns)
	{
		this.grid = Objects.requireNonNull(grid);
		this.availableFormats = Objects.requireNonNull(availableFormats);
		this.availableColumns = Objects.requireNonNull(availableColumns);
		
		// By default, all columns should be selected
		this.selectedColumns = new ArrayList<>(availableColumns);
	}
	
	public Grid<T> getGrid()
	{
		return this.grid;
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
