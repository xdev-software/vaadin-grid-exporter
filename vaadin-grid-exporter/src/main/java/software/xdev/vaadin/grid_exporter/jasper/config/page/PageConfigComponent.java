package software.xdev.vaadin.grid_exporter.jasper.config.page;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;

import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import software.xdev.vaadin.grid_exporter.Translator;
import software.xdev.vaadin.grid_exporter.format.SpecificConfigComponent;
import software.xdev.vaadin.grid_exporter.jasper.config.JasperConfigsLocalization;


public class PageConfigComponent extends SpecificConfigComponent<PageConfig>
{
	protected final ComboBox<PageType> cbPageType = new ComboBox<>();
	
	protected final ComboBox<PageOrientation> cbPageOrientation = new ComboBox<>();
	
	protected final Checkbox chbxPageNumbering = new Checkbox();
	
	protected final IntegerField intfPageMargin = new IntegerField();
	
	public PageConfigComponent(final Translator translator)
	{
		super(translator, PageConfig::new, JasperConfigsLocalization.PAGE);
		
		this.initUIs();
		
		this.registerBindings();
	}
	
	protected void initUIs()
	{
		this.cbPageType.setLabel(this.translate(JasperConfigsLocalization.FORMAT_PAGE_TYPE));
		this.cbPageType.setItemLabelGenerator(PageType::name);
		
		this.cbPageOrientation.setLabel(this.translate(JasperConfigsLocalization.ORIENTATION));
		this.cbPageOrientation.setItemLabelGenerator(po ->
			this.translate(JasperConfigsLocalization.ORIENTATION + "." + po.name().toLowerCase()));
		
		this.chbxPageNumbering.setLabel(this.translate(JasperConfigsLocalization.SHOW_PAGE_NUMBERS));
		
		this.intfPageMargin.setLabel(this.translate(JasperConfigsLocalization.MARGIN));
		this.intfPageMargin.setStepButtonsVisible(true);
		this.intfPageMargin.setMin(PageConfig.PAGE_MARGIN_MIN);
		this.intfPageMargin.setMax(PageConfig.PAGE_MARGIN_MAX);
		
		this.getContent().add(this.cbPageType, this.cbPageOrientation, this.chbxPageNumbering, this.intfPageMargin);
	}
	
	protected void registerBindings()
	{
		this.binder.forField(this.cbPageType)
			.asRequired()
			.bind(PageConfig::getSelectedPageType, PageConfig::setSelectedPageType);
		
		this.binder.forField(this.cbPageOrientation)
			.asRequired()
			.bind(PageConfig::getSelectedPageOrientation, PageConfig::setSelectedPageOrientation);
		
		this.binder.forField(this.chbxPageNumbering)
			.bind(PageConfig::isUsePageNumbering, PageConfig::setUsePageNumbering);
		
		this.binder.forField(this.intfPageMargin)
			.asRequired()
			.bind(PageConfig::getPageMargin, PageConfig::setPageMargin);
	}
	
	@Override
	public void updateFrom(final PageConfig value)
	{
		this.cbPageType.setItems(value.getAvailablePageTypes());
		
		this.cbPageOrientation.setItems(value.getAvailablePageOrientations());
		
		super.updateFrom(value);
	}
}
