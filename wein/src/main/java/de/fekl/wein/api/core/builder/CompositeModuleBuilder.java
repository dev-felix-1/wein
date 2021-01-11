package de.fekl.wein.api.core.builder;

import de.fekl.wein.api.core.IModule;

public class CompositeModuleBuilder {

	private String name;
	private CompositeRoutesBuilder routesBuilder;

	public CompositeModuleBuilder name(String name) {
		this.name = name;
		return this;
	}

	public CompositeModuleBuilder routes(CompositeRoutesBuilder routesBuilder) {
		this.routesBuilder = routesBuilder;
		return this;
	}

	public IModule build() {
		return null;

	}

}
