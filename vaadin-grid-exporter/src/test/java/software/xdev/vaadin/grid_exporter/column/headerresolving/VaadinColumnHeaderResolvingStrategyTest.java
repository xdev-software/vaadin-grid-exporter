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
package software.xdev.vaadin.grid_exporter.column.headerresolving;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Span;


class VaadinColumnHeaderResolvingStrategyTest
{
	@Test
	@DisplayName("Grid-Header is read correctly from a grid with one set column header")
	void checkSetHeaderText()
	{
		final String expectedHeader = "username";
		
		final Grid<TestUserDTO> grid = new Grid<>();
		
		final Column<TestUserDTO> colUsername = grid
			.addColumn(TestUserDTO::username)
			.setHeader(expectedHeader);
		
		final Optional<String> optResolvedName =
			new VaadinColumnHeaderResolvingStrategy().resolve(colUsername);
		
		assertTrue(optResolvedName.isPresent(), "No resolved column name found");
		assertEquals(expectedHeader, optResolvedName.get());
	}
	
	@Test
	@DisplayName("Grid-Header be found when read from a grid with a column renderer/component")
	void checkSetHeaderComponent()
	{
		final String expectedHeader = "text";
		
		final Grid<TestUserDTO> grid = new Grid<>();
		
		final Column<TestUserDTO> colUsername = grid
			.addColumn(TestUserDTO::username)
			.setHeader(new Span(expectedHeader));
		
		final Optional<String> optResolvedName =
			new VaadinColumnHeaderResolvingStrategy().resolve(colUsername);
		
		assertTrue(optResolvedName.isPresent(), "No resolved column name found");
		assertEquals(expectedHeader, optResolvedName.get());
	}
	
	@Test
	@DisplayName("Grid-Header should not be found when read from a grid with no column header")
	void checkNoHeaderSet()
	{
		final Grid<TestUserDTO> grid = new Grid<>();
		
		final Column<TestUserDTO> colUsername = grid
			.addColumn(TestUserDTO::username);
		
		final Optional<String> optResolvedName =
			new VaadinColumnHeaderResolvingStrategy().resolve(colUsername);
		
		assertFalse(optResolvedName.isPresent(), "column name found");
	}
	
	@Test
	@DisplayName("Test null safety")
	void nullSafety()
	{
		final Optional<String> optResolvedName =
			assertDoesNotThrow(
				() -> new VaadinColumnHeaderResolvingStrategy().resolve(null));
		
		assertFalse(optResolvedName.isPresent(), "column name found");
	}
	
	record TestUserDTO(String username, String firstName, String lastName)
	{
	}
}
