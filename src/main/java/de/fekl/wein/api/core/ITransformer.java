package de.fekl.wein.api.core;

import de.fekl.dine.api.core.INodeDeprecated;

public interface ITransformer<S, T> extends INodeDeprecated {

	IContentType<S> getSourceContentType();

	IContentType<T> getTargetContentType();
	
	IWsOperationIdentifier getOperation();

	IMessage<T> transform(IMessage<S> msg);

}
