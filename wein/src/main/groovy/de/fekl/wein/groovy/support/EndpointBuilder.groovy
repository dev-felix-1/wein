package de.fekl.wein.groovy.support

import de.fekl.wein.api.core.builder.WsEndpointIdentifierBuilder
import groovy.transform.CompileStatic

@CompileStatic
class EndpointBuilder extends BuilderSupport {

	@Override
	protected void setParent(Object parent, Object child) {
		println "set parent $parent on child $child"
	}

	@Override
	protected Object createNode(Object name) {
		switch(name) {
			case 'endpoint': return createEndpoint()
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Object value) {
		switch(name) {
			case 'name':  return setName(value as String)
			case 'version': return setVersion(value as String)
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes) {
		return null;
	}

	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		return null;
	}

	def createEndpoint() {
		return new WsEndpointIdentifierBuilder()
	}

	def setName(String value) {
		if (current instanceof WsEndpointIdentifierBuilder) {
			return current.name(value)
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}

	def setVersion(String value) {
		if (current instanceof WsEndpointIdentifierBuilder) {
			return current.version(value)
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}
}
