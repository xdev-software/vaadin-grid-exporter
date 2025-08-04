package software.xdev.vaadin.gridexport.example;

import java.util.List;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.grid_exporter.GridExportLocalizationConfig;
import software.xdev.vaadin.grid_exporter.GridExporter;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;
import software.xdev.vaadin.gridexport.example.jsonext.JsonGridExporterProvider;
import software.xdev.vaadin.gridexport.example.pre_defined_title.PredefinedTitleProvider;


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
				e -> GridExporter.newWithDefaults(this.grExamples)
					.open()),
			new Button(
				"Export (German translation)",
				VaadinIcon.PRINT.create(),
				e -> GridExporter.newWithDefaults(this.grExamples)
					.withLocalizationConfig(germanLocalizationConfig())
					.open()),
			new Button(
				"Export (JSON)",
				VaadinIcon.PRINT.create(),
				e -> GridExporter.newWithDefaults(this.grExamples)
					.loadFromProvider(new JsonGridExporterProvider())
					.open()),
			new Button(
				"Export (Predefined title)",
				VaadinIcon.PRINT.create(),
				e -> new GridExporter<>(this.grExamples)
					.loadFromProvider(new PredefinedTitleProvider("Custom title!"))
					.open()),
			new Button(
				"Export (Static items)",
				VaadinIcon.PRINT.create(),
				e -> GridExporter.newWithDefaults(
						this.grExamples,
						List.of(
							new Example("styled", "Styled-Demo", "dark mode 🌑 and more"),
							new Example("parameter", "Parameter-Demo", "configuration is stored in QueryParameters")
						)
					)
					.open())
		);
		
		this.grExamples
			.addColumn(Example::route)
			.setHeader("Route")
			.setFlexGrow(1);
		
		this.grExamples
			.addColumn(Example::name)
			.setHeader("Name")
			.setFlexGrow(1);
		
		this.grExamples
			.addColumn(Example::desc)
			.setHeader("Description")
			.setFlexGrow(1);
		
		this.grExamples
			.addColumn(new ComponentRenderer<>(x -> new Button("Dummy open button")))
			.setAutoWidth(true)
			.setFlexGrow(0);
		
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
	
	record Example(String route, String name, String desc)
	{
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
			.with(GridExportLocalizationConfig.ALREADY_PRESENT, "Bereits vorhanden")
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
