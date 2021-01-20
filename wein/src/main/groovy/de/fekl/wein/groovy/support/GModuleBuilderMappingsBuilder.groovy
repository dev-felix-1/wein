package de.fekl.wein.groovy.support

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

@Builder(builderStrategy = SimpleStrategy, prefix= '')
class GModuleBuilderMappingsBuilder {
	List<GModuleBuilderMappingBuilder> mappingBuilders = []
}
