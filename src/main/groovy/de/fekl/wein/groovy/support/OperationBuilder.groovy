package de.fekl.wein.groovy.support

import de.fekl.dine.api.core.INode
import de.fekl.wein.api.core.builder.EdgesBuilder
import de.fekl.wein.api.core.builder.NetBuilder
import de.fekl.wein.api.core.builder.NodeBuilder
import de.fekl.wein.api.core.builder.NodesBuilder
import de.fekl.wein.api.core.builder.OutgoingEdgesBuilder
import de.fekl.wein.api.core.builder.WsEndpointIdentifierBuilder
import de.fekl.wein.api.core.builder.WsOperationIdentifierBuilder
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class OperationBuilder extends EndpointBuilder {

	@Override
	protected void setParent(Object parent, Object child) {
	}

	@Override
	protected Object createNode(Object name) {
		switch(name) {
			case 'operation': return createOperation()
			case 'endpoint' : return setEndpoint()
			default: super.createNode(name)
		}
	}

	@Override
	protected Object createNode(Object name, Object value) {
		switch(name) {
			case 'name': return setName(value as String)
			default: super.createNode(name, value)
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

	def createOperation () {
		new WsOperationIdentifierBuilder()
	}

	def setName (String value) {
		if (current instanceof WsOperationIdentifierBuilder) {
			return current.name(value)
		}
		if (current instanceof WsEndpointIdentifierBuilder) {
			return super.setName(value)
		}
		throw new IllegalStateException();
	}

	//	@CompileDynamic
	//	def setEndpoint (Object value) {
	//		if (current instanceof WsOperationIdentifierBuilder) {
	//			if (value instanceof Closure) {
	//				current.endpoint(new EndpointBuilder().endpoint(value))
	//			}
	//		}
	//		throw new IllegalStateException();
	//	}

	@CompileDynamic
	def setEndpoint () {
		if (current instanceof WsOperationIdentifierBuilder) {
			def endpoint = super.createEndpoint()
			current.endpoint(endpoint)
			return endpoint
		}
		throw new IllegalStateException();
	}

}
