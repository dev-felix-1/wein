package de.fekl.tran.api.core;

import de.fekl.dine.api.node.INodeFactory;

public interface ITransformerFactory extends INodeFactory<ITransformer> {

	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation, String id);

	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation);

}
