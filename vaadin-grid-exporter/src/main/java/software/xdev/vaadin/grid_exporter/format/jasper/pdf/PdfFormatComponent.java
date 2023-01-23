package software.xdev.vaadin.grid_exporter.format.jasper.pdf;

import java.awt.Insets;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.FormatConfigComponent;
import software.xdev.vaadin.grid_exporter.grid.GridExportLocalizationConfig;


public class PdfFormatComponent
	extends HorizontalLayout
	implements FormatConfigComponent<PdfSpecificConfig>, Translator
{
	private ComboBox<PageOrientation> cmbPageOrientation;
	private ComboBox<PageType> cmbPageFormat;
	private Checkbox ckShowPageNumbers, ckHighlightRows;
	
	PdfFormatComponent()
	{
		this.initUI();
	}
	
	private void initUI()
	{
		this.ckShowPageNumbers = new Checkbox();
		this.ckHighlightRows = new Checkbox();
		this.cmbPageFormat = new ComboBox<>();
		this.cmbPageOrientation = new ComboBox<>();
		this.cmbPageFormat.setLabel(this.translate(GridExportLocalizationConfig.PAGE_SIZE));
		this.cmbPageOrientation.setLabel(this.translate(GridExportLocalizationConfig.PAGE_ORIENTATION));
		this.ckShowPageNumbers.setLabel(this.translate(GridExportLocalizationConfig.SHOW_PAGE_NUMBERS));
		this.ckHighlightRows.setLabel(this.translate(GridExportLocalizationConfig.HIGHLIGHT_ROWS));
		this.ckShowPageNumbers.setSizeUndefined();
		this.ckHighlightRows.setSizeUndefined();
		this.cmbPageFormat.setSizeFull();
		this.cmbPageOrientation.setSizeFull();
		
		this.cmbPageOrientation.setItems(PageOrientation.values());
		this.cmbPageOrientation.setItemLabelGenerator(orientation ->
			orientation == PageOrientation.PORTRAIT
				? this.translate(GridExportLocalizationConfig.PORTRAIT)
				: this.translate(GridExportLocalizationConfig.LANDSCAPE));
		
		this.cmbPageFormat.setItems(PageType.values());
		
		this.add(this.cmbPageFormat, this.cmbPageOrientation, this.ckShowPageNumbers, this.ckHighlightRows);
	}
	
	@Override
	public String translate(final String key)
	{
		// TODO
		return null;
	}
	
	@Override
	public PdfSpecificConfig getConfig()
	{
		return
			new PdfSpecificConfig()
				.withPageType(this.cmbPageFormat.getValue())
				.withHighlightRows(this.ckHighlightRows.getValue())
				.withPageOrientation(this.cmbPageOrientation.getValue())
				.withShowPageNumber(this.ckShowPageNumbers.getValue())
				.withPageMargin(new Insets(20, 20, 20, 20))
			;
	}
}
