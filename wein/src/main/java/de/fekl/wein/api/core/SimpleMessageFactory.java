package de.fekl.wein.api.core;

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
