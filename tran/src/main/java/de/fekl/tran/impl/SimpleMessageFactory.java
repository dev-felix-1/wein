package de.fekl.tran.impl;

import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.IMessageFactory;

public class SimpleMessageFactory implements IMessageFactory {

	@Override
	public <T> IMessage<T> createMessage(T value) {
		if (Number.class.isAssignableFrom(value.getClass())) {
			return new SimpleMessage<T>(value, (IContentType<T>) StandardContentTypes.INTEGER);
		} else if (String.class.isAssignableFrom(value.getClass())) {
			return new SimpleMessage<T>(value, (IContentType<T>) StandardContentTypes.STRING);
		}
		throw new IllegalStateException("hello - dont use this method");
	}

	@Override
	public <T> IMessage<T> copyMessage(IMessage<T> msg) {
		return new SimpleMessage<T>(msg.getValue(), msg.getContentType());
	}

}
