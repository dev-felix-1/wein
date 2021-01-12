package de.fekl.tran;

import de.fekl.dine.api.core.INode;

public interface ITransformer<S, T> extends INode {

	IContentType<S> getSourceContentType();

	IContentType<T> getTargetContentType();

	IMessage<T> transform(IMessage<S> msg);

}
