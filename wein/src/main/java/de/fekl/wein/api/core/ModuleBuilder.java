package de.fekl.wein.api.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModuleBuilder {

	private String endpointName;
	private Set<IWsEndpointIdentifier> endpoints = new HashSet<>();
	private Map<IIntegrationRouteIdentifier<?, ?>, IIntegrationRoute<?, ?>> routes = new HashMap<>();

	public ModuleBuilder endpoint(String endpointName) {
		this.endpointName = endpointName;
		return this;
	}

	public ModuleBuilder endpoint(IWsEndpointIdentifier endpoint) {
		this.endpoints.add(endpoint);
		return this;
	}

	public ModuleBuilder endpoints(Set<IWsEndpointIdentifier> endpoints) {
		this.endpoints = endpoints;
		return this;
	}

	public ModuleBuilder route(IIntegrationRoute<?, ?> route, IWsOperationIdentifier operation) {
		var source = route.getSourceContentType();
		var target = route.getTargetContentType();
		var id = new SimpleIntegrationRouteIdentifier<>(operation, source, target);
		routes.put(id, route);
		return this;
	}

	protected String getEndpoint() {
		return endpointName;
	}

	protected Set<IWsEndpointIdentifier> getEndpoints() {
		return endpoints;
	}

	protected Map<IIntegrationRouteIdentifier<?, ?>, IIntegrationRoute<?, ?>> getRoutes() {
		return routes;
	}

	public IModule build() {
		if (endpointName == null || endpointName.isBlank()) {
			endpointName = endpoints.iterator().next().getName();
		}
		return new SimpleModule(endpointName, endpoints, routes);
	}
}
