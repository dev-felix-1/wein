package de.fekl.tran;

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

	@SuppressWarnings("unchecked")
	@Override
	public ITransformer<?, ?> createNode(ITransformerFactoryParams<?, ?, ITransformer<?, ?>> params) {
		IContentType<Object> target = (IContentType<Object>) params.target();
		IContentType<Object> source = (IContentType<Object>) params.source();
		ITransformation<Object, Object> transformation = (ITransformation<Object, Object>) params.transformation();
		String id = params.id();
		if (id == null || id.isBlank()) {
			return createTransformer(source, target, transformation);
		}
		return createTransformer(source, target, transformation, id);
	}

}
