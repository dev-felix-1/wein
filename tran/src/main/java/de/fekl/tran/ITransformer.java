package de.fekl.tran;

import de.fekl.dine.api.node.INode;

public interface ITransformer<S, T> extends INode {

	IContentType<S> getSourceContentType();

	IContentType<T> getTargetContentType();

	IMessage<T> transform(IMessage<S> msg);

}
