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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.HtmlObject;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import software.xdev.vaadin.grid_exporter.Translator;


/**
 * @author XDEV Software
 * @since 10.01.00
 */
public class ReportViewerComponent extends VerticalLayout
{
	private final Anchor downloadAnchor = new Anchor();
	private final Translator translator;
	private final Button btnDownload;
	
	public ReportViewerComponent(final Translator translator)
	{
		this.translator = translator;
		this.btnDownload = new Button(
			translator.translate(GridExportLocalizationConfig.DOWNLOAD),
			VaadinIcon.DOWNLOAD.create()
		);
		this.btnDownload.setEnabled(false);
		this.btnDownload.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		this.btnDownload.addClassName(GridExporterStyles.BUTTON);
		this.downloadAnchor.add(this.btnDownload);
		this.downloadAnchor.getElement().setAttribute("download", true);
	}
	
	public Anchor getDownloadAnchor()
	{
		return this.downloadAnchor;
	}
	
	public void setData(
		final StreamResource res,
		final String mimeType
	)
	{
		this.downloadAnchor.setHref(res);
		this.btnDownload.setEnabled(true);
		
		final HtmlObject resViewer = new HtmlObject(res, mimeType);
		resViewer.addClassName(GridExporterStyles.HTML_PREVIEW);
		resViewer.getElement().setText(
			this.translator.translate(GridExportLocalizationConfig.UNABLE_TO_SHOW_PREVIEW));
		this.removeAll();
		this.add(resViewer);
	}
	
}
