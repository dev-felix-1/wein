package de.fekl.tran.impl;

import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IMessage;

public class SimpleMessage<T> implements IMessage<T> {

	private final T value;
	private final IContentType<T> contentType;

	public SimpleMessage(T value, IContentType<T> contentType) {
		this.value = value;
		this.contentType = contentType;
	}

	public SimpleMessage(SimpleMessage<T> msg) {
		this.value = msg.getValue();
		this.contentType = msg.getContentType();
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("SimpleMessage{'%s'}", value);
	}

	@Override
	public IContentType<T> getContentType() {
		return contentType;
	}

}
