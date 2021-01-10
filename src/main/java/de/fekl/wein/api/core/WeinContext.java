package de.fekl.wein.api.core;

import de.fekl.wein.api.core.registry.EndpointRegistry;

public class WeinContext {
	
	private EndpointRegistry endpointRegistry = new EndpointRegistry();
	
	public EndpointRegistry getEndpointRegistry() {
		return endpointRegistry;
	}

}
