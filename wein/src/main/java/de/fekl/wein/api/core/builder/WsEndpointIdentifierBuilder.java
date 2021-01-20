package de.fekl.wein.api.core.builder;

import de.fekl.wein.api.core.IWsEndpointIdentifier;
import de.fekl.wein.api.core.SimpleWsEndpointIdentifier;

public class WsEndpointIdentifierBuilder {

	private String name;
	private String version;

	protected String getName() {
		return name;
	}

	public WsEndpointIdentifierBuilder name(String name) {
		this.name = name;
		return this;
	}

	protected String getVersion() {
		return version;
	}

	public WsEndpointIdentifierBuilder version(String version) {
		this.version = version;
		return this;
	}

	public IWsEndpointIdentifier buildEndpoint() {
		return new SimpleWsEndpointIdentifier(name, version);
	}

}
