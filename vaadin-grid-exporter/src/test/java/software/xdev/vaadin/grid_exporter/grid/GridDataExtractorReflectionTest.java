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

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.vaadin.flow.component.grid.ColumnPathRenderer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TextRenderer;


class GridDataExtractorReflectionTest
{
	
	static Stream<Arguments> checkFormattedValue()
	{
		return Stream.of(
			new CheckFormattedValueTestData(
				"ValueProvider",
				gr -> gr.addColumn(DummyData::text)),
			new CheckFormattedValueTestData(
				"TextRenderer",
				gr -> gr.addColumn(new TextRenderer<>(DummyData::text))),
			new CheckFormattedValueTestData(
				"ColumnPathRenderer",
				gr -> gr.addColumn(new ColumnPathRenderer<>("text", DummyData::text)))
		).map(td -> Arguments.of(td.testDesc(), td.columnCreator(), td.item(), td.expectedResult()));
	}
	
	@DisplayName("Checking formattedValue")
	@ParameterizedTest(name = "{displayName} with {0}")
	@MethodSource
	<T> void checkFormattedValue(
		final String checkingDesc,
		final Function<Grid<DummyData>, Grid.Column<DummyData>> columnCreator,
		final DummyData item,
		final String expectedResult)
	{
		final Grid<DummyData> grid = new Grid<>();
		
		final Grid.Column<DummyData> column = columnCreator.apply(grid);
		
		final String value =
			Assertions.assertDoesNotThrow(() -> new GridDataExtractor<>(grid).getFormattedValue(column, item));
		
		Assertions.assertEquals(expectedResult, value);
	}
	
	record CheckFormattedValueTestData(
		String testDesc,
		Function<Grid<DummyData>, Grid.Column<DummyData>> columnCreator,
		DummyData item,
		String expectedResult
	)
	{
		CheckFormattedValueTestData(
			final String testDesc,
			final Function<Grid<DummyData>, Grid.Column<DummyData>> columnCreator)
		{
			this(testDesc, columnCreator, new DummyData("Test"), "Test");
		}
	}
	
	
	record DummyData(String text)
	{
	}
}
