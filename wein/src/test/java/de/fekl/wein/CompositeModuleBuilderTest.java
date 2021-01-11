package de.fekl.wein;

import org.junit.jupiter.api.Test;

import de.fekl.wein.api.core.builder.CompositeModuleBuilder;
import de.fekl.wein.api.core.builder.CompositeRouteBuilder;
import de.fekl.wein.api.core.builder.CompositeRoutesBuilder;

public class CompositeModuleBuilderTest {

	@Test
	public void testBuildEmptyModule() {
		//@formatter:off
		new CompositeModuleBuilder()
			.name("integration-module-test")
			.build();
		//@formatter:on
	}

	@Test
	public void testBuildModuleWithEmptyRoutes() {
		//@formatter:off
		new CompositeModuleBuilder()
			.name("integration-module-test")
			.routes(new CompositeRoutesBuilder()
				.addRoute(new CompositeRouteBuilder()
				
				)
				.addRoute(new CompositeRouteBuilder()
						
				)
			);
		//@formatter:on
	}

}
