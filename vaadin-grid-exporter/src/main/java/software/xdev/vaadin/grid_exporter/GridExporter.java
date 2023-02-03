package software.xdev.vaadin.grid_exporter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.Grid;

import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.column.ColumnConfigurationBuilder;
import software.xdev.vaadin.grid_exporter.column.ColumnConfigurationHeaderResolvingStrategyBuilder;
import software.xdev.vaadin.grid_exporter.components.wizard.GridExporterWizardState;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;
import software.xdev.vaadin.grid_exporter.jasper.format.CsvFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.DocxFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.HtmlFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.OdsFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.OdtFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.PdfFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.PptxFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.RtfFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.TextFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.XlsFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.XlsxFormat;
import software.xdev.vaadin.grid_exporter.jasper.format.XmlFormat;
import software.xdev.vaadin.grid_exporter.wizard.GridExporterWizard;


public class GridExporter<T>
{
	protected final Grid<T> grid;
	
	protected GridExportLocalizationConfig localizationConfig =
		new GridExportLocalizationConfig().withAll(JasperConfigsLocalization.DEFAULT_VALUES);
	
	protected Predicate<Grid.Column<T>> columnFilter = col -> true;
	
	protected ColumnConfigurationBuilder columnConfigurationBuilder = new ColumnConfigurationBuilder()
		.withColumnConfigHeaderResolvingStrategyBuilder(
			ColumnConfigurationHeaderResolvingStrategyBuilder::withVaadinInternalHeaderStrategy);
	
	protected String fileName = "Report_" + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm")
			.format(LocalDateTime.now(ZoneOffset.UTC));
	
	protected List<Format> availableFormats =
		new ArrayList<>(Arrays.asList(
			new PdfFormat(),
			new XlsxFormat(),
			new XlsFormat(),
			new CsvFormat(),
			new DocxFormat(),
			new HtmlFormat(),
			new OdsFormat(),
			new OdtFormat(),
			new PptxFormat(),
			new RtfFormat(),
			new TextFormat(),
			new XmlFormat()
		));
	
	protected Format preSelectedFormat = null;
	
	public GridExporter(final Grid<T> grid)
	{
		this.grid = Objects.requireNonNull(grid);
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
		
		this.availableFormats = availableFormats;
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
	
	protected List<ColumnConfiguration<T>> generateAvailableColumns()
	{
		return this.grid.getColumns().stream()
			.filter(this.columnFilter)
			.map(this.columnConfigurationBuilder::build)
			.collect(Collectors.toList());
	}
	
	public void export()
	{
		final GridExporterWizardState<T> state = new GridExporterWizardState<>(
			this.grid,
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
}
