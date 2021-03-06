package de.fekl.tran.groovy.support


import org.codehaus.groovy.runtime.InvokerHelper

import de.fekl.dine.core.api.graph.DirectedGraphBuilder
import de.fekl.dine.core.api.sponge.SpongeNetBuilder
import de.fekl.tran.api.core.IContentType
import de.fekl.tran.api.core.ITransformation
import de.fekl.tran.api.core.ITransformationRoute
import de.fekl.tran.api.core.ITransformerRegistry
import de.fekl.tran.impl.TransformationRouteBuilder
import de.fekl.tran.impl.TransformerBuilder
import de.fekl.tran.impl.TransformerNames
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

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
			case 'split': 
			case 'splitting':
			case 'splitter':
			case 'autoSplit': return setSplit(value as Boolean)
				
			case 'source': return setSource(value)
			case 'input':
			case 'inputContentType':
				case 'in': return setNodeSourceType(value)

			case 'target':  return setTarget(value)
			case 'output':
			case 'outputContentType':
				case 'out': return setNodeTargetType(value)

			case 'in/out':
				case 'i/o': return setIO(value)

			case 'id': return setId(value as String)

			case 'transform':
				case 'transformation': return setTransformation(value as ITransformation)

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
			child.parent = current
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
			def child = createRouteNodeBuilder(map.id as String)
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
			def child = createRouteNodeBuilder(id)
			current.nodeBuilders += child
			return child
		} else if (current instanceof CompositeNodesBuilder) {
			def child = createRouteNodeBuilder(id)
			current.nodeBuilders += child
			return current
		}
		throw new IllegalStateException();
	}

	def CompositeNodeBuilder createRouteNodeBuilder(String id) {
		new CompositeNodeBuilder().id(id)
	}

	def createEdge () {
		if (current instanceof CompositeEdgesBuilder) {
			def child = new CompositeEdgeBuilder()
			current.edgeBuilders += child
			return child
		}
		throw new IllegalStateException();
	}
	
	def List<CompositeNodeBuilder> createRouteNodeBuilders(Collection names) {
		names.collect { createRouteNodeBuilder(it as String) }
	}

	def createEdge (Map map) {
		if (current instanceof CompositeEdgesBuilder) {
			if (map.target instanceof Collection) {
				if (!current.parent.nodesBuilder) {
					current.parent.nodeBuilders += createRouteNodeBuilder(map.source as String)
					current.parent.nodeBuilders += createRouteNodeBuilders(map.target as Collection)
				}
				def edges = map.target.collect {
					new CompositeEdgeBuilder().source(map.source as String).target(it as String)
				}
				current.edgeBuilders += edges
				return current
			} else if (map.source instanceof Collection) {
				if (!current.parent.nodesBuilder) {
					current.parent.nodeBuilders += createRouteNodeBuilders(map.source as Collection)
					current.parent.nodeBuilders += createRouteNodeBuilder(map.target as String)
				}
				def edges = map.source.collect {
					new CompositeEdgeBuilder().source(it as String).target(map.target as String)
				}
				current.edgeBuilders += edges
				return current
			} else {
				if (!current.parent.nodesBuilder) {
					current.parent.nodeBuilders += createRouteNodeBuilder(map.source as String)
					current.parent.nodeBuilders += createRouteNodeBuilder(map.target as String)
				}
				def child = new CompositeEdgeBuilder().source(map.source as String).target(map.target as String)
				current.edgeBuilders += child
				return child
			}
		}
		throw new IllegalStateException();
	}
	
	def setSplit(Boolean value) {
		current instanceof CompositeNodeBuilder ? current.autoSplit(value) : {throw new IllegalStateException()}
	}


	def setNodeSourceType(Object value) {
		current instanceof CompositeNodeBuilder ? current.source(value) : {throw new IllegalStateException()}
	}

	def setNodeTargetType(Object value) {
		current instanceof CompositeNodeBuilder ? current.target(value) : {throw new IllegalStateException()}
	}

	def setEdgeSource(String value) {
		current instanceof CompositeEdgeBuilder ? current.source(value) : {throw new IllegalStateException()}
	}

	def setEdgeTarget(String value) {
		current instanceof CompositeEdgeBuilder ? current.target(value) : {throw new IllegalStateException()}
	}

	def setSource(Object value) {
		switch (current) {
			case { it instanceof CompositeNodeBuilder}: return setNodeSourceType(value)
			case { it instanceof CompositeEdgeBuilder}: return setEdgeSource(value as String)
			default: throw new IllegalStateException()
		}
	}

	def setTarget(Object value) {
		switch (current) {
			case { it instanceof CompositeNodeBuilder}: return setNodeTargetType(value)
			case { it instanceof CompositeEdgeBuilder}: return setEdgeTarget(value as String)
			default: throw new IllegalStateException()
		}
	}

	def setIO(Object value) {
		setNodeSourceType(value)
		setNodeTargetType(value)
	}

	def setId(String value) {
		//@formatter:off
		if 		(current instanceof CompositeNodeBuilder) 	{ current.id (value) }
		else if (current instanceof CompositeRouteBuilder) 	{ current.id (value) }
		else 												{ throw new IllegalStateException() }
		//@formatter:on
	}

	def setTransformation(ITransformation value) {
		//@formatter:off
		if (current instanceof CompositeNodeBuilder) { current.transformation ( value ) }
		else 									     { throw new IllegalStateException() }
		//@formatter:on
	}

	def injectRouteNodesFromList(List nodeNames) {
		//@formatter:off
		if (current == null) { current = createRoute() }
		//@formatter:on
		if (current instanceof CompositeRouteBuilder && !current.nodesBuilder) {
			(current.nodesBuilder = new CompositeNodesBuilder()).nodeBuilders += nodeNames.collect { createRouteNodeBuilder(it as String) }
			return current
		} else {
			throw new IllegalStateException();
		}
	}

	def injectRouteNode(String nodeName) {
		//@formatter:off
		if (current instanceof CompositeNodesBuilder) { current.addNodeBuilder(createRouteNodeBuilder(nodeName)) }
		else 									      { throw new IllegalStateException() }
		//@formatter:on
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

	@Builder(builderStrategy = SimpleStrategy, prefix= '')
	static class CompositeNodesBuilder {
		List<CompositeNodeBuilder> nodeBuilders = []

		def CompositeNodesBuilder addNodeBuilder(CompositeNodeBuilder nb) {
			nodeBuilders += nb
			return this
		}
	}

	static class CompositeEdgesBuilder {
		CompositeRouteBuilder parent
		List<CompositeEdgeBuilder> edgeBuilders = []
	}

	@Builder(builderStrategy = SimpleStrategy, prefix= '')
	static class CompositeNodeBuilder {
		String id
		boolean autoSplit
		Object source, target
		ITransformation transformation
	}

	@Builder(builderStrategy = SimpleStrategy, prefix= '')
	static class CompositeEdgeBuilder {
		String source, target
	}

	@CompileDynamic
	def call(Closure closure) {
		invokeMethod('route', closure).build()
	}

	@CompileDynamic
	def call(Map map) {
		if (map.containsKey('nodes')) {
			injectRouteNodesFromList(map.nodes).build()
		} else {
			super.call(map)
		}
	}

	@CompileDynamic
	def route(Map map) {
		if (map.containsKey('nodes')) {
			injectRouteNodesFromList(map.nodes).build()
		} else {
			super.call(map)
		}
	}

	def edge(Object source, Object target) {
		if (current instanceof CompositeEdgesBuilder) {
			return createEdge([source: source, target: target])
		} else {
			return new UnsupportedOperationException()
		}
	}
}
