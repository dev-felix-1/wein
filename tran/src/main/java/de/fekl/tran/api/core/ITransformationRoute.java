package de.fekl.tran.api.core;

import de.fekl.dine.api.base.IIdHolder;
import de.fekl.dine.api.tree.ISpongeNet;

public interface ITransformationRoute<S, T> extends IIdHolder<String>{

	ISpongeNet<ITransformer<?,?>> getGraph();

	ITransformer<S, ?> getFirst();

	ITransformer<?, T> getLast();

	IContentType<S> getSourceContentType();

	IContentType<T> getTargetContentType();
	

}
