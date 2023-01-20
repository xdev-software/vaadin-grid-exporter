
package com.rapidclipse.framework.server.reports.grid.column.headerresolving;

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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Label;


/**
 * @author XDEV Software
 *
 */
class VaadinInternalRenderingColumnHeaderResolvingStrategyTest
{
	@Test
	@DisplayName("Grid-Header is read correctly from a grid with one set column header")
	void test_001()
	{
		final String              expectedHeader  = "username";
		
		final Grid<TestUserDTO>   grid            = new Grid<>();
		
		final Column<TestUserDTO> colUsername     = grid
			.addColumn(TestUserDTO::getUsername)
			.setHeader(expectedHeader);
		
		final Optional<String>    optResolvedName =
			new VaadinInternalRenderingColumnHeaderResolvingStrategy().resolve(colUsername);
		
		assertTrue(optResolvedName.isPresent(), "No resolved column name found");
		assertEquals(expectedHeader, optResolvedName.get());
	}
	
	@Test
	@DisplayName("Grid-Header should not be found when read from a grid with no column header")
	void test_002()
	{
		final Grid<TestUserDTO>   grid            = new Grid<>();
		
		final Column<TestUserDTO> colUsername     = grid
			.addColumn(TestUserDTO::getUsername);
		
		final Optional<String>    optResolvedName =
			new VaadinInternalRenderingColumnHeaderResolvingStrategy().resolve(colUsername);
		
		assertTrue(!optResolvedName.isPresent(), "column name found");
	}
	
	@Test
	@DisplayName("Grid-Header should not be found when read from a grid with a column renderer/component")
	void test_003()
	{
		final Grid<TestUserDTO>   grid            = new Grid<>();
		
		final Column<TestUserDTO> colUsername     = grid
			.addColumn(TestUserDTO::getUsername)
			.setHeader(new Label("text"));
		
		final Optional<String>    optResolvedName =
			new VaadinInternalRenderingColumnHeaderResolvingStrategy().resolve(colUsername);
		
		assertTrue(!optResolvedName.isPresent(), "column name found");
	}
	
	@Test
	@DisplayName("Test null safety")
	void test_004()
	{
		final Optional<String> optResolvedName =
			assertDoesNotThrow(
				() -> new VaadinInternalRenderingColumnHeaderResolvingStrategy().resolve(null));
		
		assertTrue(!optResolvedName.isPresent(), "column name found");
	}
	
	public static class TestUserDTO
	{
		private final String username;
		private final String firstName;
		private final String lastName;
		
		/**
		 * @param username
		 * @param firstName
		 * @param lastName
		 */
		public TestUserDTO(final String username, final String firstName, final String lastName)
		{
			this.username  = username;
			this.firstName = firstName;
			this.lastName  = lastName;
		}
		
		/**
		 * @return the username
		 */
		public String getUsername()
		{
			return this.username;
		}
		
		/**
		 * @return the firstName
		 */
		public String getFirstName()
		{
			return this.firstName;
		}
		
		/**
		 * @return the lastName
		 */
		public String getLastName()
		{
			return this.lastName;
		}
	}
}
