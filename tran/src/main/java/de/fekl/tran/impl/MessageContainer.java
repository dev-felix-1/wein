package de.fekl.tran.impl;

import de.fekl.dine.api.state.SimpleToken;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.IMessageContainer;

public class MessageContainer extends SimpleToken implements IMessageContainer {

	private IMessage<?> message;

	public MessageContainer(String id) {
		super(id);
	}

	public MessageContainer() {
		this(MessageContainerNames.generateName());
	}

	public <T> void setMessage(IMessage<T> message) {
		this.message = message;
	}

	@SuppressWarnings("unchecked")
	public <T> IMessage<T> getMessage() {
		return (IMessage<T>) message;
	}

	@Override
	public String toString() {
		return String.format("%s('%s'){%s}", getClass().getSimpleName(), getId(), getMessage());
	}

}
