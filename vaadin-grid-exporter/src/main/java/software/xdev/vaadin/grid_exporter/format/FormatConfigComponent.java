package software.xdev.vaadin.grid_exporter.format;

import com.vaadin.flow.component.orderedlayout.FlexComponent;


public interface FormatConfigComponent<T extends SpecificConfig> extends FlexComponent
{
	T getConfig();
}
