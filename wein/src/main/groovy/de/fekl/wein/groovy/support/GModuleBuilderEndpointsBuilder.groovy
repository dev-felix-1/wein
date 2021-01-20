package de.fekl.wein.groovy.support

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class GModuleBuilderEndpointsBuilder {
	List<String> versions = []
	String name

	GModuleBuilderEndpointsBuilder name(String name) {
		this.name = name
		return this
	}

	GModuleBuilderEndpointsBuilder version(String name) {
		this.versions += name
		return this
	}

	GModuleBuilderEndpointsBuilder versions(List<String> versions) {
		this.versions = versions
		return this
	}

	@CompileDynamic
	List<GModuleBuilderEndpointBuilder> build() {
		List<GModuleBuilderEndpointBuilder> result = []
		for(def v : versions) {
			result+=new GModuleBuilderEndpointBuilder().version(v).name(name)
		}
		return result
	}
}
