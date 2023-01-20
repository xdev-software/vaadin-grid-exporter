package software.xdev.vaadin.gridexport.example;

import com.rapidclipse.framework.server.reports.grid.GridExportDialog;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("GridExport Examples")
@Route("")
public class DemoView extends Composite<VerticalLayout>
{
	private final Grid<Example> grExamples = new Grid<>();
	
	public DemoView()
	{
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
			new Button(
				"Export",
				VaadinIcon.PRINT.create(),
				e -> GridExportDialog.open(this.grExamples)),
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
}
