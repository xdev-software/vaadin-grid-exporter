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
package software.xdev.vaadin.grid_exporter.jasper;

import java.awt.Color;

import net.sf.dynamicreports.report.builder.style.SimpleStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.Styles;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;


public interface JasperGridReportStyles
{
	StyleBuilder titleStyle();
	
	StyleBuilder footerStyle();
	
	StyleBuilder columnTitleStyle();
	
	StyleBuilder columnStyle();
	
	SimpleStyleBuilder columnStyleHighlighted();
	
	static JasperGridReportStyles New()
	{
		return new Default();
	}
	
	public static class Default implements JasperGridReportStyles
	{
		protected final StyleBuilder defaultStyle = Styles.style().setPadding(2);
		protected final StyleBuilder boldCenterStyle = Styles.style(this.defaultStyle)
			.bold()
			.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		protected final StyleBuilder columnTitle = Styles.style(this.boldCenterStyle)
			.setBorder(Styles.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);
		protected final StyleBuilder columnStyle = Styles.style(this.defaultStyle)
			.setBorder(Styles.pen1Point());
		
		protected final SimpleStyleBuilder columnStyleHighlighted = Styles.simpleStyle()
			.setPadding(2)
			.setBackgroundColor(new Color(222, 222, 222)) // Extra light gray so that the data remains readable
			.setBorder(Styles.pen1Point());
		
		Default()
		{
			super();
		}
		
		@Override
		public StyleBuilder titleStyle()
		{
			return this.boldCenterStyle;
		}
		
		@Override
		public StyleBuilder footerStyle()
		{
			return this.boldCenterStyle;
		}
		
		@Override
		public StyleBuilder columnTitleStyle()
		{
			return this.columnTitle;
		}
		
		@Override
		public StyleBuilder columnStyle()
		{
			return this.columnStyle;
		}
		
		@Override
		public SimpleStyleBuilder columnStyleHighlighted()
		{
			return this.columnStyleHighlighted;
		}
	}
}
