package de.fekl.wein.groovy.support

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

class GModuleBuilderMappingBuilder {
	String operation
	String route

	GModuleBuilderMappingBuilder operation(String operation) {
		this.operation = operation
		return this
	}

	GModuleBuilderMappingBuilder route(String route) {
		this.route = route
		return this
	}
}