package de.fekl.tran.groovy.support


import org.codehaus.groovy.runtime.InvokerHelper

import de.fekl.dine.api.graph.DirectedGraphBuilder
import de.fekl.dine.api.tree.SpongeNetBuilder
import de.fekl.tran.api.core.IContentType
import de.fekl.tran.api.core.ITransformation
import de.fekl.tran.api.core.ITransformationRoute
import de.fekl.tran.api.core.ITransformerFactory
import de.fekl.tran.api.core.ITransformerRegistry
import de.fekl.tran.impl.TransformationRouteBuilder
import de.fekl.tran.impl.TransformerBuilder
import de.fekl.tran.impl.TransformerNames
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class GRouteBuilder extends BuilderSupport {
	
	ITransformerRegistry transformerRegistry

	@Override
	protected void setParent(Object parent, Object child) {
	}

	@Override
	protected Object createNode(Object name) {
		switch(name) {
			case 'route': return createRoute()
			case 'nodes': return createNodes()
			case 'node': return createNode()
			case 'edges': return createEdges()
			case 'edge': return createEdge()
			
			default: 
				if (current instanceof CompositeNodesBuilder) {
					return injectRouteNode(name as String)
				}
				throw new IllegalArgumentException("Invalid keyword $name")
			
		}
	}

	@CompileDynamic
	@Override
	protected Object createNode(Object name, Object value) {
		switch(name) {
			case 'source':
			case 'input':
			case 'inputContentType':
			case 'in': return setNodeSourceType(value)

			case 'target':
			case 'output':
			case 'outputContentType':
			case 'out': return setNodeTargetType(value)

			case 'in/out':
			case 'i/o': return setIO(value)

			case 'id': return setId(value)

			case 'transform':
			case 'transformation': return setTransformation(value)
			
			case 'node': return createRouteNode(value)
			case 'nodes': return injectRouteNodesFromList(value)
			
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes) {
		switch(name) {
			case 'edge': return createEdge(attributes)
			
			case 'transform': 
			case 'transformation': return createRouteNode(attributes)
			
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		return null;
	}
	
	public Object getProperty(String propertyName) {
		if (transformerRegistry != null && transformerRegistry.contains(propertyName) && current instanceof CompositeNodesBuilder) {
			Object name = getName('node');
			return doInvokeMethod('node', name, propertyName);
		}
	}

	public Object invokeMethod(String methodName, Object args) {
		Object name = getName(methodName);
		if (methodName == 'transform' || methodName == 'transformation') {
			def argList = InvokerHelper.asList(args);
			if(argList.size() > 1) {
				if(argList[0] instanceof Map) {
					Map argMap = argList[0]
					argMap.put('transformation', argList[1] as ITransformation)
					return doInvokeMethod(methodName, name, argMap);
				}else if(argList[0] instanceof String || argList[0] instanceof IContentType) {
					def map = ['i/o':argList[0],'transformation': argList[1] as ITransformation]
					return doInvokeMethod(methodName, name, map);
				}
			}
			else {
				return doInvokeMethod(methodName, name, InvokerHelper.asList(args)[0] as ITransformation);
			}
		} else {
			return doInvokeMethod(methodName, name, args);
		}
	}

	def createRoute () {
		def result = new CompositeRouteBuilder();
		result.transformerRegistry = transformerRegistry
		return result
	}

	def createNodes () {
		if (current instanceof CompositeRouteBuilder) {
			def child = new CompositeNodesBuilder()
			current.nodesBuilder = child
			return child
		}
		throw new IllegalStateException();
	}

	def createEdges () {
		if (current instanceof CompositeRouteBuilder) {
			def child = new CompositeEdgesBuilder()
			current.edgesBuilder = child
			return child
		}
		throw new IllegalStateException();
	}

	def createNode () {
		if (current instanceof CompositeNodesBuilder ) {
			def child = new CompositeNodeBuilder()
			current.nodeBuilders += child
			return child
		} else if (current instanceof CompositeRouteBuilder) {
			def child = new CompositeNodeBuilder()
			current.nodeBuilders += child
			return child
		}
		throw new IllegalStateException();
	}

	def createRouteNode (Map map) {
		if (current instanceof CompositeRouteBuilder) {
			def child = new CompositeNodeBuilder()
			child.id = map.id
			child.transformation = map.transformation as ITransformation
			child.source = map.source?:map.input?:map.'in'?:map.'i/o'
			child.target = map.target?:map.output?:map.'out'?:map.'i/o'
			current.nodeBuilders += child
			return child
		}
		throw new IllegalStateException();
	}
	
	def createRouteNode (String id) {
		if (current instanceof CompositeRouteBuilder) {
			def child = new CompositeNodeBuilder()
			child.id = id
			current.nodeBuilders += child
			return child
		} else if (current instanceof CompositeNodesBuilder) {
			def child = new CompositeNodeBuilder()
			child.id = id
			current.nodeBuilders += child
			return current
		}
		throw new IllegalStateException();
	}

	def createEdge () {
		if (current instanceof CompositeEdgesBuilder) {
			def child = new CompositeEdgeBuilder()
			current.edgeBuilders += child
			return child
		}
		throw new IllegalStateException();
	}

	def createEdge (Map map) {
		if (current instanceof CompositeEdgesBuilder) {
			def child = new CompositeEdgeBuilder()
			current.edgeBuilders += child
			child.source = map.source
			child.target = map.target
			return child
		}
		throw new IllegalStateException();
	}

	def setNodeSourceType(Object value) {
		if (current instanceof CompositeNodeBuilder) {
			current.source = value
			return current
		}
		throw new IllegalStateException();
	}

	def setNodeTargetType(Object value) {
		if (current instanceof CompositeNodeBuilder) {
			current.target = value
			return current
		}
		throw new IllegalStateException();
	}

	def setIO(Object value) {
		setNodeSourceType(value)
		setNodeTargetType(value)
	}

	def setId(String value) {
		if (current instanceof CompositeNodeBuilder) {
			current.id = value
			return current
		} else if (current instanceof CompositeRouteBuilder) {
			current.id ( value )
			return current
		}
		throw new IllegalStateException();
	}

	def setTransformation(ITransformation value) {
		if (current instanceof CompositeNodeBuilder) {
			current.transformation = value
			return current
		}
		throw new IllegalStateException();
	}
	
	def injectRouteNodesFromList(List nodeNames) {
		if (current == null) {
			current = createRoute()
		}
		if (current instanceof CompositeRouteBuilder) {
			current.nodesBuilder = new CompositeNodesBuilder()
			current.nodesBuilder.nodeBuilders += nodeNames.collect { name -> 
				def nb = new CompositeNodeBuilder()
				nb.id = name
				return nb
			}
			return current
		}
		throw new IllegalStateException();
	}
	
	def injectRouteNode(String nodeName) {
		if (current instanceof CompositeNodesBuilder) {
			def nb = new CompositeNodeBuilder()
			nb.id = nodeName
			current.nodeBuilders += nb
			return current
		}
		throw new IllegalStateException();
	}

	static class CompositeRouteBuilder extends TransformationRouteBuilder {
		CompositeNodesBuilder nodesBuilder
		List<CompositeNodeBuilder> nodeBuilders = []
		CompositeEdgesBuilder edgesBuilder
		ITransformerRegistry transformerRegistry

		@CompileDynamic
		@Override
		public ITransformationRoute build() {
			def spongeNetBuilder = new SpongeNetBuilder()
			def graphBuilder = new DirectedGraphBuilder();

			if (!nodesBuilder) {
				nodesBuilder = new CompositeNodesBuilder()
			}
			nodesBuilder.nodeBuilders += nodeBuilders

			nodesBuilder.nodeBuilders.each { nodeBuilder ->
				def transformerBuilder = new TransformerBuilder()
				if (transformerRegistry) {
					transformerBuilder.setRegistry(transformerRegistry)
				}
				if (!nodeBuilder.id) {
					nodeBuilder.id = TransformerNames.generateTransformerName()
				}
				graphBuilder.addNode(transformerBuilder
						.id(nodeBuilder.id)
						.source(nodeBuilder.source)
						.target(nodeBuilder.target)
						.transformation(nodeBuilder.transformation))
			}
			if (edgesBuilder) {
				edgesBuilder.edgeBuilders.each {graphBuilder.addEdge(it.source,it.target)}
			} else if(nodesBuilder.nodeBuilders.size()>1){
				for (int i = 0; i<nodesBuilder.nodeBuilders.size()-1;i++) {
					graphBuilder.addEdge(nodesBuilder.nodeBuilders[i].id,nodesBuilder.nodeBuilders[i+1].id)
				}
			}
			spongeNetBuilder.setStartNode(nodesBuilder.nodeBuilders.first().id)
			spongeNetBuilder.setGraph(graphBuilder)
			super.transformation(spongeNetBuilder)
			return super.build();
		}
	}

	static class CompositeNodesBuilder {
		List<CompositeNodeBuilder> nodeBuilders = []
	}

	static class CompositeEdgesBuilder {
		List<CompositeEdgeBuilder> edgeBuilders = []
	}

	static class CompositeNodeBuilder {
		String id
		Object source
		Object target
		ITransformation transformation
	}

	static class CompositeEdgeBuilder {
		String source
		String target
	}
	
	@CompileDynamic
	def call(Closure closure) {
		invokeMethod('route', closure).build()
	}
	
	@CompileDynamic
	def call(Map map) {
		if (map.containsKey('nodes')) {
			injectRouteNodesFromList(map.nodes).build()
		}else {
			super.call(map)
		}
	}
	
	@CompileDynamic
	def route(Map map) {
		if (map.containsKey('nodes')) {
			injectRouteNodesFromList(map.nodes).build()
		}else {
			super.call(map)
		}
	}

}
