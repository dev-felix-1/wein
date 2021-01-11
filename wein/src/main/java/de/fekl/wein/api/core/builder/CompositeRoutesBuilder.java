package de.fekl.wein.api.core.builder;

import java.util.ArrayList;
import java.util.List;

public class CompositeRoutesBuilder {

	private final List<CompositeRouteBuilder> compositeRouteBuilders = new ArrayList<>();

	public CompositeRoutesBuilder addRoute(CompositeRouteBuilder compositeRouteBuilder) {
		compositeRouteBuilders.add(compositeRouteBuilder);
		return this;
	}

}
