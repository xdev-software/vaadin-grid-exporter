package software.xdev.vaadin.grid_exporter.wizard.steps;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.HtmlObject;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.components.wizard.GridExporterWizardState;
import software.xdev.vaadin.grid_exporter.format.Format;


public class PreviewStep<T> extends AbstractGridExportWizardStepComposite<VerticalLayout, T>
{
	protected HtmlObject resViewer = new HtmlObject();
	
	public PreviewStep(final Translator translator)
	{
		super(translator);
		this.setStepName("Preview");
		
		this.initUI();
	}
	
	protected void initUI()
	{
		this.resViewer.getStyle().set("text-align", "center");
		this.resViewer.getElement().setText("Unable to show preview");
		this.resViewer.setSizeFull();
		
		this.getContent().add(this.resViewer);
		this.getContent().setPadding(false);
		this.getContent().setSizeFull();
	}
	
	@Override
	public void onEnterStep(final GridExporterWizardState<T> state)
	{
		// Generate data and preview
		final Format format = state.getSelectedFormat();
		final byte[] bytes = format.export(state.getGrid(), state.getSelectedColumns(), state.getSpecificConfigs());
		
		final StreamResource resource = new StreamResource(
			state.getFileName() + "." + format.getFormatFilenameSuffix(),
			() -> new ByteArrayInputStream(bytes)
		);
		resource.setContentType(format.getMimeType());
		
		this.resViewer.setType(format.getMimeType());
		this.resViewer.setData(resource);
		
		this.fireEvent(new StreamResourceGeneratedEvent<>(resource, this, false));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Registration addStreamResourceGeneratedListener(
		final ComponentEventListener<StreamResourceGeneratedEvent<T>> listener)
	{
		return this.addListener(StreamResourceGeneratedEvent.class, (ComponentEventListener)listener);
	}
	
	public static class StreamResourceGeneratedEvent<T> extends ComponentEvent<PreviewStep<T>>
	{
		protected final StreamResource resource;
		
		public StreamResourceGeneratedEvent(
			final StreamResource resource,
			final PreviewStep<T> source, final boolean fromClient)
		{
			super(source, fromClient);
			this.resource = Objects.requireNonNull(resource);
		}
		
		public StreamResource getResource()
		{
			return this.resource;
		}
	}
}
