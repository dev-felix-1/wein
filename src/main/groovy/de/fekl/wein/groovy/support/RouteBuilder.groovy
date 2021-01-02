package de.fekl.wein.groovy.support

import de.fekl.dine.api.core.INode
import de.fekl.wein.api.core.builder.EdgesBuilder
import de.fekl.wein.api.core.builder.NetBuilder
import de.fekl.wein.api.core.builder.NodeBuilder
import de.fekl.wein.api.core.builder.NodesBuilder
import de.fekl.wein.api.core.builder.OutgoingEdgesBuilder
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class RouteBuilder extends BuilderSupport {

	@Override
	protected void setParent(Object parent, Object child) {
		println ("parent: $parent - child: $child")
		// TODO Auto-generated method stub

	}

	@Override
	protected Object createNode(Object name) {
		println current
		switch(name) {
			case 'nodes': return createRouteNodes()
			case 'node': return createRouteNode(null)
			case 'edges': return createRouteEdges()
			case 'to': 	  return createInlineNodes()
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}


	//	return new RouteBuilder().route('testRoute') {
	//		nodes {
	//			node 'a' {
	//				role 'START'
	//			}
	//			node 'b' {
	//				impl new NodeImpl()
	//			}
	//			node 'c' {
	//				role 'END'
	//			}
	//			node 'd'
	//			node 'e'
	//		}
	//
	//		edges {
	//			from 'a' {
	//				to 'b'
	//				to 'b2'
	//			}
	//			from 'b' {
	//				to 'c'
	//			}
	//		}
	//	}
	@Override
	protected Object createNode(Object name, Object value) {
		println current
		switch(name) {
			case 'route': return createRoute(value as String)
			case 'nodes': return createRouteNodes()
			case 'node':  return createRouteNode(value as String)
			case 'role':  return setRouteNodeRole(value as String)
			case 'impl':  return setRouteNodeImpl(value)
			case 'edges': return createRouteEdges()
			case 'from':  return createOutgoing(value as String)
			case 'to':    return completeOutgoing(value as String)
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	def createRoute (String value) {
		new NetBuilder().id(value)
	}

	def createRouteNodes () {
		if (current instanceof NetBuilder) {
			def nodesBuilder = new NodesBuilder()
			(current as NetBuilder).nodes(nodesBuilder)
			return nodesBuilder
		}
		throw new IllegalStateException();
	}

	def createRouteNode (String value) {
		if (current instanceof NodesBuilder) {
			def nodeBuilder = new NodeBuilder().id(value)
			(current as NodesBuilder).node(nodeBuilder)
			return nodeBuilder
		} else if (current instanceof NetBuilder) {
			def nodeBuilder = new NodeBuilder().id(value)
			(current as NetBuilder).node(nodeBuilder)
			return nodeBuilder
		} 
		throw new IllegalStateException("current is of type ${current.class}");
	}

	def createRouteEdges () {
		if (current instanceof NetBuilder) {
			def edgesBuilder = new EdgesBuilder()
			(current as NetBuilder).edges(edgesBuilder)
			return edgesBuilder
		}
		throw new IllegalStateException();
	}

	def setRouteNodeRole (String value) {
		if (current instanceof NodeBuilder) {
			return (current as NodeBuilder).role(value)
		}
		throw new IllegalStateException();
	}

	@CompileDynamic
	def setRouteNodeImpl (Object value) {
		if (current instanceof NodeBuilder) {
			return (current as NodeBuilder).impl(value)
		}
		throw new IllegalStateException();
	}

	def createOutgoing (String value) {
		if (current instanceof EdgesBuilder) {
			def outgoing = new OutgoingEdgesBuilder().source(value)
			(current as EdgesBuilder).outgoing(outgoing)
			return outgoing
		} else if (current instanceof NetBuilder) {
			def outgoing = new OutgoingEdgesBuilder().source(value)
			(current as NetBuilder).from(outgoing)
			return outgoing
		}
		throw new IllegalStateException();
	}

	def completeOutgoing (String value) {
		if (current instanceof OutgoingEdgesBuilder) {
			(current as OutgoingEdgesBuilder).target(value)
		} else if (current instanceof NodeBuilder) {
			(current as NodeBuilder).to(value)
		}
		return current
	}
	
	def createInlineNodes() {
		if (current instanceof NodeBuilder) {
			def inlineNodeBuilder = new NodeBuilder()
			(current as NodeBuilder).to(inlineNodeBuilder)
			return inlineNodeBuilder
		}
		throw new IllegalStateException();
	}


}
