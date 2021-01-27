package de.fekl.wein.groovy.support

import org.codehaus.groovy.runtime.InvokerHelper

import de.fekl.tran.api.core.IContentType
import de.fekl.tran.api.core.ITransformation
import de.fekl.tran.api.core.ITransformerRegistry
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

@CompileStatic
class GModuleBuilder extends BuilderSupport {

	@Override
	protected void setParent(Object parent, Object child) {
	}

	@Override
	protected Object createNode(Object name) {
		switch(name) {
			case 'module' : return createModule();
			case 'endpoint': return createEndpoint()
			case 'operations': return createOperations()
			case 'operation': return createOperation()
			case 'routes' : return createRoutes();
			case 'transformers' : return createTransformers();
			case 'mappings' : return createMappings();
			case 'mapping' : return createMapping();
			case 'route': return createRoute()
			case 'nodes': return createNodes()
			case 'transformer':
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
			case 'name' : return setName(value)
			case 'version' : return setVersion(value)
			case 'versions' : return setVersions(value)

			case 'route' : return setRoute(value)
			case 'operation' : return setOperation(value)

			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes) {
		switch(name) {
			case 'edge': return createEdge(attributes)
			case 'endpoint': return createEndpoint(attributes)

			case 'transform':
				case 'transformation': return createRouteNode(attributes)

			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		return null;
	}

	def createModule() {
		new GModuleBuilderModuleBuilder()
	}

	def createRoutes() {
		if (current instanceof GModuleBuilderModuleBuilder) {
			def routesBuilder = new GModuleBuilderRoutesBuilder()
			current.routesBuilder = routesBuilder
			return routesBuilder
		} else {
			throw new IllegalStateException()
		}
	}

	def createOperations() {
		if (current instanceof GModuleBuilderModuleBuilder) {
			def opsBuilder = new CompositeOperationsBuilder()
			current.opsBuilder = opsBuilder
			return opsBuilder
		} else {
			throw new IllegalStateException()
		}
	}

	def createTransformers() {
		if (current instanceof GModuleBuilderModuleBuilder) {
			def routesBuilder = new CompositeTransformersBuilder()
			current.transformersBuilder = routesBuilder
			return routesBuilder
		} else {
			throw new IllegalStateException()
		}
	}

	def createMappings() {
		if (current instanceof GModuleBuilderModuleBuilder) {
			def mappings = new GModuleBuilderMappingsBuilder()
			current.mappings(mappings)
			return mappings
		} else {
			throw new IllegalStateException()
		}
	}

	def createMapping() {
		if (current instanceof GModuleBuilderMappingsBuilder) {
			def mappingBuilder = new GModuleBuilderMappingBuilder()
			current.mappingBuilders += mappingBuilder
			return mappingBuilder
		} else {
			throw new IllegalStateException()
		}
	}

	def createEndpoint() {
		if (current instanceof GModuleBuilderModuleBuilder) {
			def eb = new GModuleBuilderEndpointsBuilder()
			current.endpointsBuilder = eb
			return eb
		} else if (current instanceof GModuleBuilderOperationBuilder) {
			def eb = new GModuleBuilderEndpointBuilder()
			current.endpointBuilder = eb
			return eb
		}
		else {
			throw new IllegalStateException("current is of type ${current.class}");
		}
	}

	def createEndpoint(Map attributes) {
		if (current instanceof GModuleBuilderModuleBuilder) {
			def eb = new GModuleBuilderEndpointsBuilder()
			eb.name(attributes.name as String)
			if (attributes.versions) {
				eb.versions(attributes.versions as List)
			} else if (attributes.version) {
				eb.version(attributes.version as String)
			}
			current.endpointsBuilder = eb
			return current
		} else if (current instanceof GModuleBuilderOperationBuilder) {
			def eb = new GModuleBuilderEndpointBuilder()
			eb.name(attributes.name as String)
			eb.version(attributes.version as String)
			current.endpointBuilder = eb
			return current
		} else if (current instanceof CompositeOperationsBuilder) {
			def eb = new GModuleBuilderEndpointsBuilder()
			eb.name(attributes.name as String)
			if (attributes.versions) {
				eb.versions(attributes.versions as List)
			} else if (attributes.version) {
				eb.version(attributes.version as String)
			}
			current.endpointsBuilder = eb
			return current
		}
		else {
			throw new IllegalStateException("current is of type ${current.class}");
		}
	}

	def createOperation () {
		if (current instanceof CompositeOperationsBuilder) {
			def ob = new  GModuleBuilderOperationBuilder()
			current.opBuilders += ob
			return ob
		} else {
			throw new IllegalStateException()
		}
	}

	def createRoute() {
		if (current instanceof GModuleBuilderRoutesBuilder) {
			def result = new GModuleBuilderRouteBuilder();
			result.transformerRegistry = transformerRegistry
			current.routeBuilders += result
			return result
		} else {
			throw new IllegalStateException()
		}
	}

	static class CompositeTransformersBuilder {
		def List<CompositeNodeBuilder> transformerBuilders = []
	}

	static class CompositeOperationsBuilder {
		def List<GModuleBuilderOperationBuilder> opBuilders = []
		def GModuleBuilderEndpointsBuilder endpointsBuilder
	}

	def setOperation(String value) {
		current instanceof GModuleBuilderMappingBuilder ? current.operation(value) : {throw new IllegalStateException()}
	}

	def setRoute(String value) {
		current instanceof GModuleBuilderMappingBuilder ? current.route(value) : {throw new IllegalStateException()}
	}


	ITransformerRegistry transformerRegistry


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


	def createNodes () {
		assert current instanceof GModuleBuilderRouteBuilder
		def child = new CompositeNodesBuilder()
		current.nodesBuilder = child
		return child
	}

	def createEdges () {
		current instanceof GModuleBuilderRouteBuilder
		def child = new CompositeEdgesBuilder()
		child.parent = current
		current.edgesBuilder = child
		return child
	}

	def createNode () {
		if (current instanceof CompositeNodesBuilder ) {
			def child = new CompositeNodeBuilder()
			current.add(child)
			return child
		} else if (current instanceof GModuleBuilderRouteBuilder) {
			def child = new CompositeNodeBuilder()
			current.nodeBuilders += child
			return child
		}
		else if (current instanceof CompositeTransformersBuilder) {
			def child = new CompositeNodeBuilder()
			current.transformerBuilders += child
			return child
		}
		throw new IllegalStateException();
	}

	def createRouteNode (Map map) {
		assert current instanceof GModuleBuilderRouteBuilder
		def child = createRouteNodeBuilder(map.id as String)
		child.transformation = map.transformation as ITransformation
		child.source = map.source?:map.input?:map.'in'?:map.'i/o'
		child.target = map.target?:map.output?:map.'out'?:map.'i/o'
		current.nodeBuilders += child
		return child
	}

	def createRouteNode (String id) {
		if (current instanceof GModuleBuilderRouteBuilder) {
			def child = createRouteNodeBuilder(id)
			current.nodeBuilders += child
			return child
		} else if (current instanceof CompositeNodesBuilder) {
			def child = createRouteNodeBuilder(id)
			current.add(child)
			return current
		}
		throw new IllegalStateException();
	}

	def CompositeNodeBuilder createRouteNodeBuilder(String id) {
		new CompositeNodeBuilder().id(id)
	}

	def createEdge () {
		assert current instanceof CompositeEdgesBuilder
		def child = new CompositeEdgeBuilder()
		current.add(child)
		return child
	}

	def List<CompositeNodeBuilder> createRouteNodeBuilders(Collection names) {
		names.collect { createRouteNodeBuilder(it as String) }
	}

	def createEdge (Map map) {
		assert current instanceof CompositeEdgesBuilder
		if (map.target instanceof Collection) {
			def edges = map.target.collect {
				new CompositeEdgeBuilder().source(map.source as String).target(it as String)
			}
			current.addAll(edges)
			return current
		} else if (map.source instanceof Collection) {
			def edges = map.source.collect {
				new CompositeEdgeBuilder().source(it as String).target(map.target as String)
			}
			current.addAll(edges)
			return current
		} else {
			def child = new CompositeEdgeBuilder().source(map.source as String).target(map.target as String)
			current.add(child)
			return child
		}
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
		if 		(current instanceof CompositeNodeBuilder) 		{ current.id (value) }
		else if (current instanceof GModuleBuilderRouteBuilder) { current.id (value) }
		else 													{ throw new IllegalStateException() }
		//@formatter:on
	}

	def setTransformation(ITransformation value) {
		//@formatter:off
		if (current instanceof CompositeNodeBuilder) { current.transformation ( value ) }
		else 									     { throw new IllegalStateException() }
		//@formatter:on
	}

	def setName(String value) {
		if (current instanceof GModuleBuilderEndpointsBuilder) {
			return current.name(value)
		}
		if (current instanceof GModuleBuilderEndpointBuilder) {
			return current.name(value)
		}
		if (current instanceof GModuleBuilderOperationBuilder) {
			return current.name(value)
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}

	def setVersion(String value) {
		if (current instanceof GModuleBuilderEndpointsBuilder ) {
			return current.version(value)
		}
		if (current instanceof GModuleBuilderEndpointBuilder) {
			return current.version(value)
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}

	def setVersions(List<String> values) {
		if (current instanceof GModuleBuilderEndpointsBuilder) {
			return current.versions(values)
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}

	def injectRouteNodesFromList(List nodeNames) {
		//@formatter:off
		if (current == null) { current = createRoute() }
		//@formatter:on
		if (current instanceof GModuleBuilderRouteBuilder && !current.nodesBuilder) {
			current.nodesBuilder = new CompositeNodesBuilder()
			current.nodesBuilder.addAll( nodeNames.collect { createRouteNodeBuilder(it as String) } )
			return current
		} else {
			throw new IllegalStateException();
		}
	}

	def injectRouteNode(String nodeName) {
		//@formatter:off
		if (current instanceof CompositeNodesBuilder) { current.add(createRouteNodeBuilder(nodeName)) }
		else 									      { throw new IllegalStateException() }
		//@formatter:on
	}

	static class CompositeNodesBuilder extends GListBuilder<CompositeNodeBuilder,GModuleBuilderRouteBuilder>{}

	static class CompositeEdgesBuilder extends GListBuilder<CompositeEdgeBuilder,GModuleBuilderRouteBuilder>{}

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
