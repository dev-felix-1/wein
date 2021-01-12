package de.fekl.tran;

import de.fekl.baut.Precondition;

public class SimpleTransformer<S, T> implements ITransformer<S, T> {

	private final IContentType<S> sourceContentType;
	private final IContentType<T> targetContentType;
	private final ITransformation<S, T> transformation;
	private final String id;

	public SimpleTransformer(IContentType<S> sourceContentType, IContentType<T> targetContentType,
			ITransformation<S, T> transformation, String id) {
		super();
		Precondition.isNotNull(sourceContentType);
		Precondition.isNotNull(targetContentType);
		Precondition.isNotNull(transformation);
		Precondition.isNotEmpty(id);
		this.sourceContentType = sourceContentType;
		this.targetContentType = targetContentType;
		this.transformation = transformation;
		this.id = id;
	}

	@Override
	public IContentType<S> getSourceContentType() {
		return sourceContentType;
	}

	@Override
	public IContentType<T> getTargetContentType() {
		return targetContentType;
	}

	@Override
	public IMessage<T> transform(IMessage<S> msg) {
		S value = msg.getValue();
		T transformed = transformation.transform(value);
		return new SimpleMessageFactory().createMessage(transformed);
	}

	@Override
	public String toString() {
		return String.format("%s{in=%s,out=%s,operation=%s}", getClass().getSimpleName(), sourceContentType,
				targetContentType);
	}

	@Override
	public String getId() {
		return id;
	}

}