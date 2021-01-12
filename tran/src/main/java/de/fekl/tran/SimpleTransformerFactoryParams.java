package de.fekl.tran;

public record SimpleTransformerFactoryParams<S, T> (String id, IContentType<S> source, IContentType<T> target,
		ITransformation<S, T> transformation) implements ITransformerFactoryParams<S, T, ITransformer<S, T>> {

}
