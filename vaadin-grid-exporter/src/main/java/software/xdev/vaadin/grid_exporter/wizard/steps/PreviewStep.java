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
package software.xdev.vaadin.grid_exporter.wizard.steps;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.HtmlObject;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import software.xdev.vaadin.grid_exporter.GridExportLocalizationConfig;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.Format;
import software.xdev.vaadin.grid_exporter.wizard.GridExporterWizardState;


public class PreviewStep<T> extends AbstractGridExportWizardStepComposite<VerticalLayout, T>
{
	protected HtmlObject resViewer = new HtmlObject();
	
	public PreviewStep(final Translator translator)
	{
		super(translator);
		this.setStepName(this.translate(GridExportLocalizationConfig.PREVIEW));
		
		this.initUI();
	}
	
	protected void initUI()
	{
		this.resViewer.getStyle().set("text-align", "center");
		this.resViewer.getElement().setText(this.translate(GridExportLocalizationConfig.UNABLE_TO_SHOW_PREVIEW));
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
		final byte[] bytes = format.export(
			state.getGridDataExtractor(),
			state.getSelectedColumns(),
			state.getSpecificConfigs());
		
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
