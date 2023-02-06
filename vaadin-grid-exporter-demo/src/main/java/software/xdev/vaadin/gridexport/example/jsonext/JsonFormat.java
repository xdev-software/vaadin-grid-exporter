package software.xdev.vaadin.gridexport.example.jsonext;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import software.xdev.vaadin.grid_exporter.column.ColumnConfiguration;
import software.xdev.vaadin.grid_exporter.format.AbstractFormat;
import software.xdev.vaadin.grid_exporter.format.SpecificConfig;
import software.xdev.vaadin.grid_exporter.grid.GridDataExtractor;


public class JsonFormat extends AbstractFormat
{
	public JsonFormat()
	{
		super("JSON", "json", "application/json");
		this.withConfigComponents(
			JsonConfigComponent::new
		);
	}
	
	@Override
	public <T> byte[] export(
		final GridDataExtractor<T> gridDataExtractor,
		final List<ColumnConfiguration<T>> columnsToExport,
		final List<? extends SpecificConfig> configs)
	{
		final ObjectMapper mapper = new ObjectMapper();
		// pretty writer
		final ObjectWriter writer =
			this.getValueFrom(configs, JsonConfig.class, JsonConfig::isUsePrettyPrint)
				.orElse(false)
				? mapper.writerWithDefaultPrettyPrinter()
				: mapper.writer();
		
		try
		{
			final List<List<String>> rowData =
				gridDataExtractor.getSortedAndFilteredData(columnsToExport);
			return writer.writeValueAsBytes(
				this.getValueFrom(configs, JsonConfig.class, JsonConfig::isWithKeys)
					.orElse(false)
					// Create a key-value Map
					? rowData.stream()
					.map(list -> columnsToExport.stream().collect(Collectors.toMap(
						ColumnConfiguration::getKeyOrHeader,
						i -> list.get(columnsToExport.indexOf(i))))
					)
					.collect(Collectors.toList())
					: rowData);
		}
		catch(final JsonProcessingException e)
		{
			throw new RuntimeException(e);
		}
	}
}
