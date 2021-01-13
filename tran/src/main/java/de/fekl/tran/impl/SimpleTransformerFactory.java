package de.fekl.tran.impl;

import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.ITransformation;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.api.core.ITransformerFactory;

public class SimpleTransformerFactory implements ITransformerFactory {

	@Override
	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation, String id) {
		return new SimpleTransformer<>(sourceContentType, targetContentType, transformation, id);
	}

	@Override
	public <S, T> ITransformer<S, T> createTransformer(IContentType<S> sourceContentType,
			IContentType<T> targetContentType, ITransformation<S, T> transformation) {
		return createTransformer(sourceContentType, targetContentType, transformation,
				TransformerNames.generateTransformerName());
	}

}
