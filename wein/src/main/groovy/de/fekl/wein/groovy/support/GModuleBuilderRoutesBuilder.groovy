package de.fekl.wein.groovy.support

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

@Builder(builderStrategy = SimpleStrategy, prefix= '')
class GModuleBuilderRoutesBuilder {
	List<GModuleBuilderRouteBuilder> routeBuilders = []
}
