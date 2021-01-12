package de.fekl.wein.groovy.support

import org.codehaus.groovy.runtime.InvokerHelper

import de.fekl.tran.IContentType
import de.fekl.tran.ITransformation
import de.fekl.tran.StandardContentTypes
import de.fekl.wein.api.core.IWsEndpointIdentifier
import de.fekl.wein.api.core.IWsOperationIdentifier
import de.fekl.wein.api.core.builder.WsEndpointIdentifierBuilder
import groovy.transform.CompileStatic

@CompileStatic
class TransformerBuilder extends BuilderSupport {

	@Override
	protected void setParent(Object parent, Object child) {
		println "set parent $parent on child $child"
	}

	@Override
	protected Object createNode(Object name) {
		switch(name) {
			case 'transformer': return createTransformer()
//			case 'transform': return createTransformation()
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Object value) {
		switch(name) {
			case 'transformer': return createTransformer(value as String)
			case 'name':  return setName(value as String)
			case 'operation': return setOperation(value as String)
			case 'input': return setInput(value)
			case 'output': return setOutput(value)
			case 'transform': return createTransformation(value)
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

	def createTransformer() {
		return new de.fekl.wein.api.core.builder.TransformerBuilder()
	}
	
	def createTransformer(String value) {
		return new de.fekl.wein.api.core.builder.TransformerBuilder()
	}

//	def createTransformation() {
//		if (current instanceof de.fekl.wein.api.core.builder.TransformerBuilder) {
//			return current
//		}
//	}
	
	public Object invokeMethod(String methodName, Object args) {
		Object name = getName(methodName);
		if (methodName == 'transform') {
			return doInvokeMethod(methodName, name, InvokerHelper.asList(args)[0] as ITransformation);
		} else {
			return doInvokeMethod(methodName, name, args);
		}
	}
	
	def createTransformation(Object value) {
		if (current instanceof de.fekl.wein.api.core.builder.TransformerBuilder) {
			if(value instanceof Closure || ITransformation) {
				return current.transformation(value as ITransformation)
			}
		}
	}

	def setName(String value) {
		if (current instanceof de.fekl.wein.api.core.builder.TransformerBuilder) {
			return current
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}

	def setOperation(String value) {
		if (current instanceof de.fekl.wein.api.core.builder.TransformerBuilder) {
			return current.operation(new IWsOperationIdentifier() {

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public IWsEndpointIdentifier getEndpoint() {
					// TODO Auto-generated method stub
					return null;
				}
			})
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}

	def setInput(Object value) {
		if (current instanceof de.fekl.wein.api.core.builder.TransformerBuilder) {
			if(value instanceof IContentType) {
				return current.sourceContentType(value)
			}
			if(value instanceof CharSequence) {
				return current.sourceContentType(StandardContentTypes.byName(value as String))
			}
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}

	def setOutput(Object value) {
		if (current instanceof de.fekl.wein.api.core.builder.TransformerBuilder) {
			if(value instanceof IContentType) {
				return current.targetContentType(value)
			}
			if(value instanceof CharSequence) {
				return current.targetContentType(StandardContentTypes.byName(value as String))
			}
		}
		throw new IllegalStateException("current is of type ${current.class}");
	}
}
