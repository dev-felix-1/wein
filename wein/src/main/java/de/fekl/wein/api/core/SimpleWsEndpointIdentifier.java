package de.fekl.wein.api.core;

import java.util.Objects;

import de.fekl.dine.util.Precondition;

public class SimpleWsEndpointIdentifier implements IWsEndpointIdentifier {

	private final String name;
	private final String version;

	public SimpleWsEndpointIdentifier(String name, String version) {
		super();
		Precondition.isNotEmpty(name);
		Precondition.isNotEmpty(version);
		this.name = name;
		this.version = version;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return String.format("%s{name=%s,version=%s}", this.getClass().getSimpleName(), name, version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(IWsEndpointIdentifier.class, name, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IWsEndpointIdentifier other) {
			return name.equals(other.getName()) && version.equals(other.getVersion());
		}
		return false;
	}

}
