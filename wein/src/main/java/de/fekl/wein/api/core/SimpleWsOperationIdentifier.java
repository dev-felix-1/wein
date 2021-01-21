package de.fekl.wein.api.core;

import java.util.Objects;

import de.fekl.dine.util.Precondition;

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

	@Override
	public int hashCode() {
		return Objects.hash(IWsEndpointIdentifier.class, name, endpoint);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IWsOperationIdentifier other) {
			return name.equals(other.getName()) && endpoint.equals(other.getEndpoint());
		}
		return false;
	}

}
