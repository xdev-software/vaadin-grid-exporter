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
package software.xdev.vaadin.grid_exporter.format.jasper.pdf;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.grid.Grid;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.Components;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.FormatConfigComponent;
import software.xdev.vaadin.grid_exporter.format.GeneralConfig;
import software.xdev.vaadin.grid_exporter.format.jasper.AbstractJasperReportFormatter;
import software.xdev.vaadin.grid_exporter.format.jasper.GridDataSourceFactory;
import software.xdev.vaadin.grid_exporter.format.jasper.GridReportStyles;


public class PdfFormat<T> extends AbstractJasperReportFormatter<T, PdfSpecificConfig>
{
	
	public PdfFormat(final Translator translator)
	{
		super(JasperReportBuilder::toPdf, "PDF", "pdf", "application/pdf", translator);
	}
	
	@Override
	public boolean isPreviewableInStandardBrowser()
	{
		return true;
	}
	
	@Override
	public FormatConfigComponent<PdfSpecificConfig> createConfigurationComponent()
	{
		return new PdfFormatComponent(this.getTranslator());
	}
	
	@Override
	public boolean isPaginationActive()
	{
		return true;
	}
	
	@Override
	protected JasperReportBuilder buildReport(
		final Grid<T> gridToExport,
		final GeneralConfig<T> generalConfig,
		final PdfSpecificConfig specificConfig)
	{
		final JasperReportBuilder report = super.buildReport(gridToExport, generalConfig, specificConfig);
		
		if(specificConfig.isShowPageNumber())
		{
			report.pageFooter(DynamicReports.cmp.pageXofY().setStyle(GridReportStyles.New().footerStyle()));
		}
		
		if(specificConfig.isHighlightRows())
		{
			report.highlightDetailOddRows();
		}
		
		report.setShowColumnTitle(true);
		report.setDataSource(new GridDataSourceFactory.Default<T>().createDataSource(gridToExport, generalConfig));
		report.setPageFormat(specificConfig.getPageType(), specificConfig.getPageOrientation());
		
		final String title = specificConfig.getTitle();
		if(!StringUtils.isEmpty(title))
		{
			report.title(Components.text(title).setStyle(this.getGridReportStyles().titleStyle()));
			report.setReportName(title);
		}
		else
		{
			report.setReportName("GridExport");
		}
		
		report.setPageMargin(DynamicReports.margin(20));
		
		return report;
	}
}
