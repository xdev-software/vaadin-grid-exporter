package software.xdev.vaadin.gridexport.example.jsonext;

import software.xdev.vaadin.grid_exporter.GridExporterProvider;


public class JsonGridExporterProvider extends GridExporterProvider
{
	public JsonGridExporterProvider()
	{
		super(JsonConfigComponent.DEFAULT_VALUES, new JsonFormat());
	}
}
