package de.fekl.wein.groovy.support

import de.fekl.wein.api.core.builder.WsEndpointIdentifierBuilder

class TestEndpointBuilder {

	public WsEndpointIdentifierBuilder testClosureBuilder() {

		return new EndpointBuilder().endpoint {
			name ('testEndpoint')
			version ('0.1')
		}
	}
}
