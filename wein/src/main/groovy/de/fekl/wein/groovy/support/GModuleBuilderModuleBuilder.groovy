package de.fekl.wein.groovy.support

import de.fekl.wein.api.core.IIntegrationRoute
import de.fekl.wein.api.core.IModule
import de.fekl.wein.api.core.IWsOperationIdentifier
import de.fekl.wein.api.core.ModuleBuilder
import de.fekl.wein.api.core.builder.WsEndpointIdentifierBuilder
import de.fekl.wein.groovy.support.GModuleBuilder.CompositeOperationsBuilder
import de.fekl.wein.groovy.support.GModuleBuilder.CompositeTransformersBuilder

class GModuleBuilderModuleBuilder extends ModuleBuilder {
	GModuleBuilderRoutesBuilder routesBuilder
	CompositeTransformersBuilder transformersBuilder
	CompositeOperationsBuilder opsBuilder
	GModuleBuilderMappingsBuilder mappingsBuilder
	GModuleBuilderEndpointsBuilder endpointsBuilder

	public ModuleBuilder putRoute(IIntegrationRoute<?, ?> route, IWsOperationIdentifier operation) {
		return super.route(route, operation);
	}

	public ModuleBuilder mappings(GModuleBuilderMappingsBuilder mappingsBuilder) {
		this.mappingsBuilder = mappingsBuilder
		return this
	}

	@Override
	IModule build() {

		if (transformersBuilder) {
			def nodeBuilders = transformersBuilder.transformerBuilders
			routesBuilder.routeBuilders.each { it.nodeBuilders+=nodeBuilders }
		}

		opsBuilder.opBuilders.each {
			if (!it.endpoint && opsBuilder.endpointsBuilder) {
				it.endpoint ( new GModuleBuilderEndpointsBuilder()
						.name(opsBuilder.endpointsBuilder.name)
						.versions(opsBuilder.endpointsBuilder.versions)
						.build().first().buildEndpoint())
			}
		}

		Map<String,GModuleBuilderRouteBuilder> routesMap = routesBuilder.routeBuilders.collect{[it.id, it]}.collectEntries()
		Map<String,GModuleBuilderOperationBuilder> opsMap = opsBuilder.opBuilders.collect{[it.name, it]}.collectEntries()
		mappingsBuilder.mappingBuilders.each { mb ->
			def route = routesMap[mb.route]
			def op = opsMap[mb.operation]
			putRoute(
					route.build() as IIntegrationRoute,
					op.build() as IWsOperationIdentifier)
		}

		if (endpointsBuilder) {
			endpointsBuilder.build().each {
				endpoint(it.buildEndpoint())
			}
		} else {
			opsBuilder.opBuilders.each { endpoint(it.build().endpoint) }
		}

		return super.build()
	}
}
