package de.fekl.wein.groovy.support

import de.fekl.wein.api.core.builder.WsOperationIdentifierBuilder

class TestOperationBuilder {

	public WsOperationIdentifierBuilder testClosureBuilder() {

		return new OperationBuilder().operation {
			name ('testOperation')
			endpoint {
				name ('web-api-1')
				version('0.1')
			}
		}
	}
}
