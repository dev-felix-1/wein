package de.fekl.wein.api.core;

import de.fekl.baut.Precondition;

public class SimpleWsOperationIdentifier implements IWsOperationIdentifier {

	private final IWsEndpointIdentifier endpoint;
	private final String name;

	public SimpleWsOperationIdentifier(IWsEndpointIdentifier endpoint, String name) {
		super();
		Precondition.isNotNull(endpoint);
		Precondition.isNotEmpty(name);
		this.endpoint = endpoint;
		this.name = name;
	}

	@Override
	public IWsEndpointIdentifier getEndpoint() {
		return endpoint;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("%s{name=%s,endpoint=%s}", this.getClass().getSimpleName(), name, endpoint);
	}

}
