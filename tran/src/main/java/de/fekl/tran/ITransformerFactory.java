package de.fekl.tran;

import de.fekl.dine.api.node.INodeFactory;

public interface ITransformerFactory
		extends INodeFactory<ITransformer<?, ?>, ITransformerFactoryParams<?, ?, ITransformer<?, ?>>> {

	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation, String id);

	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation);

}
