package de.fekl.tran.api.core;

import java.util.List;

import de.fekl.dine.api.node.INodeFactory;

public interface ITransformerFactory extends INodeFactory<ITransformer<?, ?>> {

	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation, String id);

	public <T> IMerger<T> createMerger(List<IContentType<?>> sourceContentTypes, IContentType<T> targetContentType,
			IMerge<T> transformation, String id);

	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation, String id, boolean autoSplit);

	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation);

}
