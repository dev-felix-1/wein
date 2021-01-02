package de.fekl.wein.api.core;

import de.fekl.baut.Precondition;

public class SimpleTransformer<S, T> implements ITransformer<S, T> {

	private final IContentType<S> sourceContentType;
	private final IContentType<T> targetContentType;

	private final IWsOperationIdentifier operation;
	private final ITransformation<S, T> transformation;

	public SimpleTransformer(IContentType<S> sourceContentType, IContentType<T> targetContentType,
			IWsOperationIdentifier operation, ITransformation<S, T> transformation) {
		super();
		Precondition.isNotNull(sourceContentType);
		Precondition.isNotNull(targetContentType);
		Precondition.isNotNull(operation);
		Precondition.isNotNull(transformation);
		this.sourceContentType = sourceContentType;
		this.targetContentType = targetContentType;
		this.operation = operation;
		this.transformation = transformation;
	}

	@Override
	public String print() {
		return toString();
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
	public IWsOperationIdentifier getOperation() {
		return operation;
	}

	@Override
	public IMessage<T> transform(IMessage<S> msg) {
		S value = msg.getValue();
		T transformed = transformation.transform(value);
		return new SimpleMessage<>(transformed);
	}

	@Override
	public String toString() {
		return String.format("%s{in=%s,out=%s,operation=%s}", getClass().getSimpleName(), sourceContentType,
				targetContentType, operation);
	}

}
