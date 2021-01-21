package de.fekl.tran.impl;

import de.fekl.dine.util.Precondition;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IFormatter;
import de.fekl.tran.api.core.IFormattingTransformation;
import de.fekl.tran.api.core.IMessage;

public class SimpleFormatter<T> implements IFormatter<T> {

	private final IContentType<T> contentType;
	private final IFormattingTransformation<T> transformation;
	private final String id;

	public SimpleFormatter(IContentType<T> contentType, IFormattingTransformation<T> transformation, String id) {
		super();
		Precondition.isNotNull(contentType);
		Precondition.isNotNull(transformation);
		Precondition.isNotEmpty(id);
		this.contentType = contentType;
		this.transformation = transformation;
		this.id = id;
	}

	@Override
	public IContentType<T> getSourceContentType() {
		return contentType;
	}

	@Override
	public IMessage<T> transform(IMessage<T> msg) {
		T value = msg.getValue();
		T transformed = transformation.transform(value);
		return new SimpleMessageFactory().createMessage(transformed);
	}

	@Override
	public String toString() {
		return String.format("%s{%s}", getClass().getSimpleName(), contentType);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean isAutoSplit() {
		return false;
	}

}
