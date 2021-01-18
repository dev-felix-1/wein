package de.fekl.tran.groovy.support


import java.util.concurrent.Callable

import org.codehaus.groovy.runtime.InvokerHelper

import de.fekl.dine.api.graph.DirectedGraphBuilder
import de.fekl.dine.api.tree.SpongeNetBuilder
import de.fekl.tran.api.core.IContentType
import de.fekl.tran.api.core.IMerge
import de.fekl.tran.api.core.ITransformation
import de.fekl.tran.api.core.ITransformationRoute
import de.fekl.tran.api.core.ITransformer
import de.fekl.tran.api.core.ITransformerRegistry
import de.fekl.tran.impl.MergerBuilder
import de.fekl.tran.impl.StandardContentTypes
import de.fekl.tran.impl.TransformationRouteBuilder
import de.fekl.tran.impl.TransformerBuilder
import de.fekl.tran.impl.TransformerNames
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class GTransformerBuilder extends BuilderSupport {
	
	ITransformerRegistry transformerRegistry
	boolean autoRegister

	@Override
	protected void setParent(Object parent, Object child) {
	}

	@Override
	protected Object createNode(Object name) {
		switch(name) {
			case 'transformer': return createTransformer()
			default: throw new IllegalArgumentException("Invalid keyword $name")
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
			
			case 'source':
			case 'input':
			case 'inputContentType':
			case 'sourceContentType':
			case 'in': return setNodeSourceType(value)

			case 'target':
			case 'output':
			case 'outputContentType':
			case 'targetContentType':
			case 'out': return setNodeTargetType(value)

			case 'in/out':
			case 'i/o': return setIO(value)

			case 'id': return setId(value)

			case 'transform':
			case 'transformation': return setTransformation(value)
			
			case 'merge': return setMerge(value)

			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes) {
		switch(name) {

			case 'transform':
			case 'transformer':
			case 'transformation': return createTransformer(attributes)

			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		return null;
	}

	public Object invokeMethod(String methodName, Object args) {
		Object name = getName(methodName);
		if (current != null && (methodName == 'transformer' || methodName == 'transform' || methodName == 'transformation' || methodName == 'merge')) {
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
				if(methodName == 'merge') {
					return doInvokeMethod(methodName, name, InvokerHelper.asList(args)[0] as IMerge);
				}else {
					return doInvokeMethod(methodName, name, InvokerHelper.asList(args)[0] as ITransformation);
				}
			}
		} else {
			return doInvokeMethod(methodName, name, args);
		}
	}

	def createTransformer () {
		def result = new CompositeTransformerBuilder();
		result.registry = transformerRegistry
		result.autoRegister = autoRegister
		return result
	}

	@CompileDynamic
	def createTransformer (Map map) {
		def result = createTransformer () 
		result.id(map.id as String)
		result.transformation(map.transformation as ITransformation)
		result.source(map.source?:map.input?:map.'in'?:map.'i/o')
		result.target(map.target?:map.output?:map.'out'?:map.'i/o')
		return result
	}
	
	def setSplit(Boolean value) {
		if (current instanceof CompositeTransformerBuilder) {
			current.autoSplit(value)
			return current
		}
		throw new IllegalStateException();
	}

	@CompileDynamic
	def setNodeSourceType(Object value) {
		if (current instanceof CompositeTransformerBuilder) {
			current.source(value)
			return current
		}
		throw new IllegalStateException();
	}

	@CompileDynamic
	def setNodeTargetType(Object value) {
		if (current instanceof CompositeTransformerBuilder) {
			current.target(value)
			return current
		}
		throw new IllegalStateException();
	}

	def setIO(Object value) {
		setNodeSourceType(value)
		setNodeTargetType(value)
	}

	def setId(String value) {
		if (current instanceof CompositeTransformerBuilder) {
			current.id(value)
			return current
		}
		throw new IllegalStateException();
	}
	
	def setMerge(IMerge value) {
		if (current instanceof CompositeTransformerBuilder) {
			current.merger = true
			current.transformation(value)
			return current
		}
		throw new IllegalStateException();
	}

	def setTransformation(ITransformation value) {
		if (current instanceof CompositeTransformerBuilder) {
			current.transformation(value)
			return current
		}
		throw new IllegalStateException();
	}

	static class CompositeTransformerBuilder extends TransformerBuilder {
		boolean merger = false
		List<IContentType> sourceContentTypes = []
		
		@Override
		public TransformerBuilder source(IContentType sourceContentType) {
			sourceContentTypes+=sourceContentType
			return this
		}
		
		@Override
		public TransformerBuilder source(String sourceContentType) {
			sourceContentTypes+=StandardContentTypes.byName(sourceContentType)
			return this
		}
		
		public ITransformer build() {
			if (merger) {
				def mergerBuilder = new MergerBuilder()
				mergerBuilder.autoLookUp = autoLookUp
				mergerBuilder.autoRegister = autoRegister
				mergerBuilder.autoSplit = autoSplit
				mergerBuilder.id(id)
				mergerBuilder.registry = registry
				mergerBuilder.sourceContentTypes = sourceContentTypes
				mergerBuilder.targetContentType = targetContentType
				mergerBuilder.transformation = transformation as IMerge
				return mergerBuilder.build()
			} else {
				super.source(sourceContentTypes.last())
				return super.build()
			}
		}
	}
	
	@CompileDynamic
	def call(Map map, Closure closure) {
		def argMap = map
		argMap += ['transformation' : closure as ITransformation]
		Object name = getName('transformer');
		def builder = doInvokeMethod('transformer', name, argMap);
		builder.build()
	}
	
	@CompileDynamic
	def call(Closure closure) {
		Object name = getName('transformer');
		def builder = doInvokeMethod('transformer',name, closure);
		builder.build()
	}

}
