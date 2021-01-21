package de.fekl.wein.api.core;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.dine.util.Precondition;

public class SimpleModule implements IModule {

	private String endpointName;
	private Set<IWsEndpointIdentifier> endpoints;
	private final Map<IIntegrationRouteIdentifier<?, ?>, IIntegrationRoute<?, ?>> routes;

	public SimpleModule(String endpointName, Set<IWsEndpointIdentifier> endpoints,
			Map<IIntegrationRouteIdentifier<?, ?>, IIntegrationRoute<?, ?>> routes) {
		Precondition.isNotNull(endpoints);
		Precondition.isNotNull(routes);
		Precondition.isNotEmpty(endpointName);
		for (IWsEndpointIdentifier endpointId : endpoints) {
			if (!endpointId.getName().equals(endpointName)) {
				throw new IllegalStateException(
						String.format("You can only define one endpointName for a module - conflict %s %s",
								endpointId.getName(), endpointName));
			}
		}
		this.endpointName = endpointName;
		this.endpoints = endpoints;
		this.routes = Collections.unmodifiableMap(routes);
	}

	@Override
	public Set<IWsEndpointIdentifier> getEndpoints() {
		return endpoints;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S, T> IIntegrationRoute<S, T> getRoute(IIntegrationRouteIdentifier<S, T> id) {
		return (IIntegrationRoute<S, T>) routes.get(id);
	}

	@Override
	public Map<IIntegrationRouteIdentifier<?, ?>, IIntegrationRoute<?, ?>> getRoutes() {
		return routes;
	}

	@Override
	public String getEndpointName() {
		return endpointName;
	}

	@Override
	public String toString() {
		var printTemplate = """
					Module for Endpoint %s
					- versions: [%s]
					- routes: [%s]
				""";
		return String.format(printTemplate, endpointName,
				getEndpoints().stream().map(e -> e.getVersion()).collect(Collectors.joining(", ")),
				routes.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList())

		);
	}

	@Override
	public Set<IWsOperationIdentifier> getOperations() {
		throw new UnsupportedOperationException();
	}

}
