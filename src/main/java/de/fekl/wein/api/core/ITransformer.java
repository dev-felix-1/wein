package de.fekl.wein.api.core;

import de.fekl.dine.api.graph.INode;

public interface ITransformer<S, T> extends INode {

	IContentType<S> getSourceContentType();

	IContentType<T> getTargetContentType();

	IWsOperationIdentifier getOperation();

	IMessage<T> transform(IMessage<S> msg);

}
