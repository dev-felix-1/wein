package de.fekl.wein.groovy.support

import de.fekl.wein.api.core.IWsOperationIdentifier
import de.fekl.wein.api.core.builder.WsEndpointIdentifierBuilder
import de.fekl.wein.api.core.builder.WsOperationIdentifierBuilder

class GModuleBuilderOperationBuilder extends WsOperationIdentifierBuilder {
	WsEndpointIdentifierBuilder endpointBuilder

	@Override
	public IWsOperationIdentifier build() {
		if (!endpoint) {
			endpoint(endpointBuilder.buildEndpoint())
		}
		return super.build();
	}
}
