package de.fekl.tran;

import de.fekl.dine.api.tree.ISpongeNet;

public interface ITransformationRoute<S, T> {

	@SuppressWarnings("rawtypes")
	ISpongeNet<ITransformer> getGraph();

	ITransformer<S, ?> getFirst();

	ITransformer<?, T> getLast();

	IContentType<S> getSourceContentType();

	IContentType<T> getTargetContentType();

}
