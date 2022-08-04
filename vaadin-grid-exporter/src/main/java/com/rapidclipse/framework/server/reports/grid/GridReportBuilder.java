package com.rapidclipse.framework.server.reports.grid;

/*-
 * #%L
 * vaadin-grid-exporter
 * %%
 * Copyright (C) 2022 XDEV Software
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.commons.lang3.StringUtils;

import com.rapidclipse.framework.server.reports.grid.column.ColumnConfiguration;
import com.vaadin.flow.component.grid.ColumnTextAlign;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;


public interface GridReportBuilder<T>
{
	JasperReportBuilder buildReport(GridExportConfiguration<T> configuration);
	
	static <T> GridReportBuilder<T> New()
	{
		return new Default<>(GridDataSourceFactory.New(), GridReportStyles.New());
	}
	
	static <T> GridReportBuilder<T> New(final GridDataSourceFactory<T> dataSourceFactory)
	{
		return new Default<>(dataSourceFactory, GridReportStyles.New());
	}
	
	static <T> GridReportBuilder<T> New(final GridReportStyles styles)
	{
		return new Default<>(GridDataSourceFactory.New(), styles);
	}
	
	static <T> GridReportBuilder<T> New(
		final GridDataSourceFactory<T> dataSourceFactory,
		final GridReportStyles styles)
	{
		return new Default<>(dataSourceFactory, styles);
	}
	
	class Default<T> implements GridReportBuilder<T>
	{
		protected final GridDataSourceFactory<T> dataSourceFactory;
		protected final GridReportStyles styles;
		
		Default(
			final GridDataSourceFactory<T> dataSourceFactory,
			final GridReportStyles styles)
		{
			this.dataSourceFactory = dataSourceFactory;
			this.styles = styles;
		}
		
		@Override
		public JasperReportBuilder buildReport(final GridExportConfiguration<T> configuration)
		{
			final JasperReportBuilder report = DynamicReports.report();
			
			configuration.getColumnConfigurations().stream()
				.filter(ColumnConfiguration::isVisible)
				.map(this::toReportColumn)
				.forEach(report::addColumn);
			
			report.setColumnTitleStyle(this.styles.columnTitleStyle());
			report.setColumnStyle(this.styles.columnStyle());
			
			final String title = configuration.getTitle();
			if(!StringUtils.isEmpty(title))
			{
				report.title(Components.text(title).setStyle(this.styles.titleStyle()));
				report.setReportName(title);
			}
			else
			{
				report.setReportName("GridExport");
			}
			
			if(configuration.isShowPageNumber())
			{
				report.pageFooter(DynamicReports.cmp.pageXofY().setStyle(this.styles.footerStyle()));
			}
			
			if(configuration.isHighlightRows())
			{
				report.highlightDetailOddRows();
			}
			
			report.setIgnorePagination(!configuration.getFormat().isPaginationActive());
			
			report.setShowColumnTitle(true);
			report.setDataSource(this.dataSourceFactory.createDataSource(configuration));
			report.setPageFormat(configuration.getPageType(), configuration.getPageOrientation());
			
			report.setPageMargin(
				configuration.getFormat().hasPageMargin() ? DynamicReports.margin(20) : DynamicReports.margin(0));
			
			return report;
		}
		
		private TextColumnBuilder<String> toReportColumn(final ColumnConfiguration<T> column)
		{
			final String fieldName = column.getGridColumn().getKey() != null
				? column.getGridColumn().getKey()
				: column.getHeader();
			final TextColumnBuilder<String> reportColumn =
				Columns.column(column.getHeader(), fieldName, String.class);
			
			final Integer width = column.getColumnWidth();
			if(width != null && width > 0)
			{
				reportColumn.setFixedWidth(width);
			}
			
			reportColumn.setHorizontalTextAlignment(
				this.toReportTextAlignment(column.getColumnAlignment()));
			
			return reportColumn;
		}
		
		private HorizontalTextAlignment toReportTextAlignment(final ColumnTextAlign alignment)
		{
			switch(alignment)
			{
				case END:
					return HorizontalTextAlignment.RIGHT;
				
				case CENTER:
					return HorizontalTextAlignment.CENTER;
				
				default:
					return HorizontalTextAlignment.LEFT;
			}
		}
	}
}
