package de.fekl.tran.impl;

import de.fekl.dine.util.Precondition;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformation;
import de.fekl.tran.api.core.ITransformer;

public class SimpleTransformer<S, T> implements ITransformer<S, T> {
	
	public static boolean ENFORCE_NULLSAFE_TRANSFORMATION = true;

	private final IContentType<S> sourceContentType;
	private final IContentType<T> targetContentType;
	private final ITransformation<S, T> transformation;
	private final String id;
	private final boolean autoSplit;

	public SimpleTransformer(IContentType<S> sourceContentType, IContentType<T> targetContentType,
			ITransformation<S, T> transformation, String id, boolean autoSplit) {
		super();
		Precondition.isNotEmpty(id);
		Precondition.isNotNull(sourceContentType, "Tried to construct transformer %s without %s", id,
				"sourceContentType");
		Precondition.isNotNull(targetContentType);
		Precondition.isNotNull(transformation);
		if (ENFORCE_NULLSAFE_TRANSFORMATION) {
			try {
				transformation.transform(null);
			} catch (Exception e) {
				throw new IllegalArgumentException(
						String.format("The transformation for transformer %s is not null-safe or has other problems: ", id),
						e);
			}
		}
		this.sourceContentType = sourceContentType;
		this.targetContentType = targetContentType;
		this.transformation = transformation;
		this.id = id;
		this.autoSplit = autoSplit;
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
		return String.format("%s{in=%s,out=%s}", getClass().getSimpleName(), sourceContentType, targetContentType);
	}

	@Override
	public String getId() {
		return id;
	}

	protected ITransformation<S, T> getTransformation() {
		return transformation;
	}

	@Override
	public boolean isAutoSplit() {
		return autoSplit;
	}

}
