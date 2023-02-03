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
package software.xdev.vaadin.grid_exporter.format;

import java.util.Objects;
import java.util.function.Supplier;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.grid_exporter.Translator;


public abstract class SpecificConfigComponent<T extends SpecificConfig>
	extends Composite<FormLayout>
	implements Translator
{
	protected final Translator translator;
	protected final Supplier<T> newConfigSupplier;
	protected final String header;
	
	protected Binder<T> binder = new Binder<>();
	
	protected SpecificConfigComponent(
		final Translator translator,
		final Supplier<T> newConfigSupplier,
		final String headerToTranslate)
	{
		this.translator = Objects.requireNonNull(translator);
		this.newConfigSupplier = Objects.requireNonNull(newConfigSupplier);
		this.header = this.translate(Objects.requireNonNull(headerToTranslate));
		
		this.getContent().setResponsiveSteps(
			new FormLayout.ResponsiveStep("0", 1),
			new FormLayout.ResponsiveStep("400px", 2)
		);
	}
	
	public Supplier<T> getNewConfigSupplier()
	{
		return this.newConfigSupplier;
	}
	
	public String getHeader()
	{
		return this.header;
	}
	
	public void updateFrom(final T value)
	{
		this.binder.setBean(value);
	}
	
	public T getBean()
	{
		return this.binder.getBean();
	}
	
	public boolean isValid()
	{
		return this.binder.isValid();
	}
	
	@Override
	public String translate(final String key)
	{
		return this.translator != null ? this.translator.translate(key) : key;
	}
}
