package de.fekl.tran.impl;

import java.util.List;

import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IMerge;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.ITransformation;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.api.core.ITransformerFactory;

public class SimpleTransformerFactory implements ITransformerFactory {

	@Override
	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation, String id) {
		return new SimpleTransformer<>(sourceContentType, targetContentType, transformation, id, false);
	}

	@Override
	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation, String id, boolean autoSplit) {
		return new SimpleTransformer<>(sourceContentType, targetContentType, transformation, id, autoSplit);
	}

	@Override
	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation) {
		return createTransformer(sourceContentType, targetContentType, transformation,
				TransformerNames.generateTransformerName());
	}

	@Override
	public <T> IMerger<T> createMerger(List<IContentType<?>> sourceContentTypes, IContentType<T> targetContentType,
			IMerge<T> transformation, String id) {
		return createMerger(sourceContentTypes, targetContentType, transformation, id, false);
	}

	@Override
	public <T> IMerger<T> createMerger(List<IContentType<?>> sourceContentTypes, IContentType<T> targetContentType,
			IMerge<T> transformation, String id, boolean autoSplit) {
		return new SimpleMerger<T>(sourceContentTypes, targetContentType, transformation, id, autoSplit);
	}

	@Override
	public <T> IMerger<T> createMerger(List<IContentType<?>> sourceContentTypes, IContentType<T> targetContentType,
			IMerge<T> transformation) {
		return createMerger(sourceContentTypes, targetContentType, transformation,
				TransformerNames.generateTransformerName(), false);
	}

}
