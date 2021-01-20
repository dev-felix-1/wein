package de.fekl.wein.api.core;

import java.util.Map;
import java.util.Set;

public interface IModule {

	String getEndpointName();

	Set<IWsEndpointIdentifier> getEndpoints();
	
	Set<IWsOperationIdentifier> getOperations();

	<S, T> IIntegrationRoute<S, T> getRoute(IIntegrationRouteIdentifier<S, T> id);

	Map<IIntegrationRouteIdentifier<?, ?>, IIntegrationRoute<?, ?>> getRoutes();

}
