package de.fekl.wein.api.core.builder;

import de.fekl.wein.api.core.IModule;
import de.fekl.wein.api.core.ModuleNames;

public class CompositeModuleBuilder {

	private String name;
	private boolean autoInject = false;
	private CompositeRoutesBuilder routesBuilder;

	public CompositeModuleBuilder name(String name) {
		this.name = name;
		return this;
	}

	public CompositeModuleBuilder autoInject() {
		this.autoInject = true;
		return this;
	}

	public CompositeModuleBuilder routes(CompositeRoutesBuilder routesBuilder) {
		this.routesBuilder = routesBuilder;
		return this;
	}

	public IModule build() {
		if (name == null || name.isBlank()) {
			name = ModuleNames.generateName();
		}
		return null;

	}

}
