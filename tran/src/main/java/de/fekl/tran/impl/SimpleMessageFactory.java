package de.fekl.tran.impl;

import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.IMessageFactory;

public class SimpleMessageFactory implements IMessageFactory {

	@Override
	public <T> IMessage<T> createMessage(T value) {
		return new SimpleMessage<T>(value);
	}

	@Override
	public <T> IMessage<T> copyMessage(IMessage<T> msg) {
		return new SimpleMessage<T>(msg.getValue());
	}

}
