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
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.Format;


/**
 * @author XDEV Software
 * @since 10.01.00
 */
public class ReportViewerDialog extends Dialog implements AfterNavigationObserver
{
	private final VerticalLayout rootLayout = new VerticalLayout();
	
	private final HorizontalLayout headerbar = new HorizontalLayout();
	private final Button btnClose = new Button(VaadinIcon.CLOSE.create());
	private final Anchor downloadAnchor = new Anchor();
	private final Label lblTitle = new Label("Report");
	
	private final Accordion previewAccordion = new Accordion();
	private final AccordionPanel previewAccordionPanel;
	
	private final HtmlObject resViewer;
	
	protected ReportViewerDialog(
		final StreamResource res,
		final String mimeType,
		final boolean isPreviewableInStandardBrowser,
		final Translator translator)
	{
		this.previewAccordionPanel = new AccordionPanel(
			translator.translate(GridExportLocalizationConfig.PREVIEW),
			new Div());
		this.initUI();
		
		this.lblTitle.setText(res.getName());
		
		this.resViewer = new HtmlObject(res, mimeType);
		this.resViewer.setMinHeight("60vh");
		this.resViewer.setSizeFull();
		this.resViewer.setMaxWidth("100%");
		this.resViewer.setMaxHeight("100%");
		this.resViewer.getElement().setText(
			translator.translate(GridExportLocalizationConfig.UNABLE_TO_SHOW_PREVIEW));
		
		this.previewAccordionPanel.setContent(this.resViewer);
		if(isPreviewableInStandardBrowser)
		{
			this.previewAccordion.open(this.previewAccordionPanel);
		}
		else
		{
			this.previewAccordion.close();
		}
		
		final Button btnDownload = new Button(
			translator.translate(GridExportLocalizationConfig.DOWNLOAD),
			VaadinIcon.DOWNLOAD.create());
		btnDownload.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		this.downloadAnchor.add(btnDownload);
		this.downloadAnchor.setHref(res);
	}
	
	public ReportViewerDialog(
		final StreamResource res,
		final Format format,
		final Translator translator)
	{
		this(res, format.getMimeType(), format.isPreviewableInStandardBrowser(), translator);
	}
	
	private void initUI()
	{
		this.btnClose.addClickListener(event -> this.close());
		
		this.headerbar.setSpacing(true);
		this.headerbar.setPadding(false);
		this.headerbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		this.headerbar.setFlexGrow(1.0, this.lblTitle);
		this.headerbar.setWidthFull();
		this.headerbar.add(this.lblTitle, this.downloadAnchor, this.btnClose);
		
		this.previewAccordion.setSizeFull();
		this.previewAccordion.add(this.previewAccordionPanel);
		
		this.rootLayout.setSpacing(true);
		this.rootLayout.setPadding(false);
		this.rootLayout.setWidth("70vw");
		this.rootLayout.setMaxHeight("80vh");
		this.rootLayout.add(this.headerbar, this.previewAccordion);
		
		this.add(this.rootLayout);
		this.setSizeUndefined();
	}
	
	/**
	 * @return the rootLayout
	 */
	public VerticalLayout getRootLayout()
	{
		return this.rootLayout;
	}
	
	/**
	 * @return the resViewer
	 */
	public HtmlObject getResViewer()
	{
		return this.resViewer;
	}
	
	@Override
	public void afterNavigation(final AfterNavigationEvent event)
	{
		// Workaround for https://github.com/vaadin/vaadin-dialog-flow/issues/108
		this.close();
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
