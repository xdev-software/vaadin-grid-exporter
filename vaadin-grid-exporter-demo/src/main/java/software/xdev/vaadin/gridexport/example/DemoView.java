package software.xdev.vaadin.gridexport.example;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.grid_exporter.GridExportLocalizationConfig;
import software.xdev.vaadin.grid_exporter.GridExporter;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


/**
 * Shows the simple usage of the {@link GridExportDialog}.
 *
 * @author JohannesRabauer
 * @author AB
 */
@PageTitle("GridExport Examples")
@Route("")
public class DemoView extends Composite<VerticalLayout>
{
	private final Grid<Example> grExamples = new Grid<>();
	
	public DemoView()
	{
		final HorizontalLayout hlButtonContainer = new HorizontalLayout();
		hlButtonContainer.setPadding(false);
		hlButtonContainer.add(
			new Button(
				"Export",
				VaadinIcon.PRINT.create(),
				e -> new GridExporter<>(this.grExamples).export()),
			new Button(
				"Export (German translation)",
				VaadinIcon.PRINT.create(),
				e -> new GridExporter<>(this.grExamples).withLocalizationConfig(germanLocalizationConfig()).export())
		);
		
		this.grExamples
			.addColumn(Example::getRoute)
			.setHeader("Route")
			.setFlexGrow(1);
		
		this.grExamples
			.addColumn(Example::getName)
			.setHeader("Name")
			.setFlexGrow(1);
		
		this.grExamples
			.addColumn(Example::getDesc)
			.setHeader("Description")
			.setFlexGrow(1);
		
		this.grExamples.setSizeFull();
		this.grExamples.addThemeVariants(GridVariant.LUMO_COMPACT);
		
		this.getContent().add(
			hlButtonContainer,
			this.grExamples);
		this.getContent().setHeightFull();
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.grExamples.setItems(
			new Example("styled", "Styled-Demo", "dark mode 🌑 and more"),
			new Example("parameter", "Parameter-Demo", "configuration is stored in QueryParameters"),
			new Example("localized", "Localized-Demo", "🌐 simple localization"),
			new Example("customized", "Customized-Demo", "usage of a customized DateRange")
		);
	}
	
	static class Example
	{
		private final String route;
		private final String name;
		private final String desc;
		
		public Example(final String route, final String name, final String desc)
		{
			super();
			this.route = route;
			this.name = name;
			this.desc = desc;
		}
		
		public String getRoute()
		{
			return this.route;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		public String getDesc()
		{
			return this.desc;
		}
	}
	
	static GridExportLocalizationConfig germanLocalizationConfig()
	{
		return new GridExportLocalizationConfig()
			.with(GridExportLocalizationConfig.EXPORT_GRID, "Tabelle exportieren")
			.with(GridExportLocalizationConfig.CANCEL, "Abbrechen")
			.with(GridExportLocalizationConfig.PREVIOUS, "Zurück")
			.with(GridExportLocalizationConfig.NEXT, "Weiter")
			.with(GridExportLocalizationConfig.DOWNLOAD, "Herunterladen")
			.with(GridExportLocalizationConfig.GENERAL, "Generell")
			.with(GridExportLocalizationConfig.FILENAME, "Dateiname")
			.with(GridExportLocalizationConfig.COLUMNS, "Spalten")
			.with(GridExportLocalizationConfig.NAME, "Name")
			.with(GridExportLocalizationConfig.POSITION, "Position")
			.with(GridExportLocalizationConfig.FORMAT, "Format")
			.with(GridExportLocalizationConfig.PREVIEW, "Vorschau")
			.with(GridExportLocalizationConfig.UNABLE_TO_SHOW_PREVIEW, "Vorschau kann nicht angezeigt werden")
			.with(JasperConfigsLocalization.ENCODING, "Enkodierung")
			.with(JasperConfigsLocalization.WITH_BOM, "mit BOM")
			.with(JasperConfigsLocalization.HEADER, "Header")
			.with(JasperConfigsLocalization.EXPORT_HEADER, "Kopfzeilen exportieren")
			.with(JasperConfigsLocalization.HIGHLIGHTING, "Hervorhebungen")
			.with(JasperConfigsLocalization.HIGHLIGHT_ROWS, "Zeilen hervorheben")
			.with(JasperConfigsLocalization.PAGE, "Seite")
			.with(JasperConfigsLocalization.FORMAT_PAGE_TYPE, "Format / Seitentyp")
			.with(JasperConfigsLocalization.ORIENTATION, "Orientierung")
			.with(JasperConfigsLocalization.ORIENTATION_PORTRAIT, "Hochformat")
			.with(JasperConfigsLocalization.ORIENTATION_LANDSCAPE, "Querformat")
			.with(JasperConfigsLocalization.SHOW_PAGE_NUMBERS, "Seitennummerierung")
			.with(JasperConfigsLocalization.MARGIN, "Rand")
			.with(JasperConfigsLocalization.SEPARATOR, "Trennzeichen")
			.with(JasperConfigsLocalization.TITLE, "Titel");
	}
}
