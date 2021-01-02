package de.fekl.wein.api.core;

import de.fekl.wein.api.core.registry.EndpointRegistry;

public class WineContext {
	
	private EndpointRegistry endpointRegistry = new EndpointRegistry();
	
	public EndpointRegistry getEndpointRegistry() {
		return endpointRegistry;
	}

}
