package de.fekl.tran;

import de.fekl.dine.api.node.INodeFactoryParams;

public interface ITransformerFactoryParams<S, T, R extends ITransformer<S, T>> extends INodeFactoryParams<R> {

	public IContentType<S> source();

	public IContentType<T> target();
	
	public ITransformation<S, T> transformation();

}
