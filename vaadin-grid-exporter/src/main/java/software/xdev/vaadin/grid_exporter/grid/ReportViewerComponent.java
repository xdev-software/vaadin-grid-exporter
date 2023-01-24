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
package software.xdev.vaadin.grid_exporter.grid;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

import software.xdev.vaadin.grid_exporter.Translator;


/**
 * @author XDEV Software
 * @since 10.01.00
 */
public class ReportViewerComponent extends VerticalLayout
{
	private final VerticalLayout rootLayout = new VerticalLayout();
	
	private final HorizontalLayout headerbar = new HorizontalLayout();
	private final Anchor downloadAnchor = new Anchor();
	private StreamResource res;
	private String mimeType;
	private boolean isPreviewableInStandardBrowser;
	private Translator translator;
	
	private final Accordion previewAccordion = new Accordion();
	
	public void setData(
		final StreamResource res,
		final String mimeType,
		final boolean isPreviewableInStandardBrowser,
		final Translator translator
	)
	{
		this.res = res;
		this.mimeType = mimeType;
		this.isPreviewableInStandardBrowser = isPreviewableInStandardBrowser;
		this.translator = translator;
		this.initUI();
	}
	
	private void initUI()
	{
		final Button btnDownload = new Button(
			this.translator.translate(GridExportLocalizationConfig.DOWNLOAD),
			VaadinIcon.DOWNLOAD.create());
		btnDownload.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		this.downloadAnchor.add(btnDownload);
		this.downloadAnchor.setHref(this.res);
		
		this.headerbar.setSpacing(true);
		this.headerbar.setPadding(false);
		this.headerbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		this.headerbar.setWidthFull();
		this.headerbar.add(this.downloadAnchor);
		
		this.previewAccordion.setSizeFull();
		final AccordionPanel previewAccordionPanel = new AccordionPanel(
			this.translator.translate(GridExportLocalizationConfig.PREVIEW),
			new Div());
		this.previewAccordion.add(previewAccordionPanel);
		
		final HtmlObject resViewer = new HtmlObject(this.res, this.mimeType);
		resViewer.setMinHeight("60vh");
		resViewer.setSizeFull();
		resViewer.setMaxWidth("100%");
		resViewer.setMaxHeight("100%");
		resViewer.getElement().setText(
			this.translator.translate(GridExportLocalizationConfig.UNABLE_TO_SHOW_PREVIEW));
		
		previewAccordionPanel.setContent(resViewer);
		if(this.isPreviewableInStandardBrowser)
		{
			this.previewAccordion.open(previewAccordionPanel);
		}
		else
		{
			this.previewAccordion.close();
		}
		
		this.setSpacing(true);
		this.setPadding(false);
		this.setWidth("70vw");
		this.setMaxHeight("80vh");
		this.add(this.headerbar, this.previewAccordion);
		
		this.setSizeUndefined();
	}
	
	/**
	 * @return the rootLayout
	 */
	public VerticalLayout getRootLayout()
	{
		return this.rootLayout;
	}
	
	@Tag(Tag.OBJECT)
	static class HtmlObject extends HtmlComponent
	{
		private static final PropertyDescriptor<String, String> DATA =
			PropertyDescriptors.attributeWithDefault("data", "");
		
		private static final PropertyDescriptor<String, String> TYPE =
			PropertyDescriptors.attributeWithDefault("type", "");
		
		/**
		 * Creates a new html object with a data resource and a type.
		 *
		 * @see #setData(AbstractStreamResource)
		 * @see #setType(String)
		 */
		public HtmlObject(final AbstractStreamResource data, final String type)
		{
			this.setData(data);
			this.setType(type);
		}
		
		/**
		 * Gets the data URL.
		 *
		 * @return the data URL of this html object
		 */
		public String getData()
		{
			return this.get(DATA);
		}
		
		/**
		 * Sets the data URL of this html object.
		 *
		 * @param data
		 */
		public void setData(final String data)
		{
			this.set(DATA, data);
		}
		
		/**
		 * Sets the data URL with the URL of the given {@link StreamResource}.
		 *
		 * @param data
		 *            the resource value, not null
		 */
		public void setData(final AbstractStreamResource data)
		{
			this.getElement().setAttribute(DATA.getPropertyName(), data);
		}
		
		/**
		 * Gets the mime type of this html object.
		 *
		 * @return the mime type of this html object
		 */
		public String getType()
		{
			return this.get(TYPE);
		}
		
		/**
		 * Sets the mime type of this html object.
		 *
		 * @param type
		 *            the new mime type
		 * @see https://www.iana.org/assignments/media-types/media-types.xhtml
		 */
		public void setType(final String type)
		{
			this.set(TYPE, type);
		}
	}
}
