package de.fekl.wein.api.core.builder;

import de.fekl.wein.api.core.IWsEndpointIdentifier;
import de.fekl.wein.api.core.IWsOperationIdentifier;
import de.fekl.wein.api.core.SimpleWsOperationIdentifier;

public class WsOperationIdentifierBuilder {

	private IWsEndpointIdentifier endpoint;
	private WsEndpointIdentifierBuilder endpointBuilder;
	private String name;

	public IWsEndpointIdentifier getEndpoint() {
		return endpoint;
	}

	public String getName() {
		return name;
	}

	public WsOperationIdentifierBuilder endpoint(IWsEndpointIdentifier endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	public WsOperationIdentifierBuilder endpoint(WsEndpointIdentifierBuilder endpointBuilder) {
		this.endpointBuilder = endpointBuilder;
		return this;
	}

	public WsOperationIdentifierBuilder name(String name) {
		this.name = name;
		return this;
	}

	public IWsOperationIdentifier build() {
		if (endpoint != null) {
			return new SimpleWsOperationIdentifier(endpoint, name);
		} else {
			return new SimpleWsOperationIdentifier(endpointBuilder.buildEndpoint(), name);
		}
	}
}
