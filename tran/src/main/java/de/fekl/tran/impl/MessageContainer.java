package de.fekl.tran.impl;

import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.SimpleToken;
import de.fekl.tran.api.core.IMessage;

public class MessageContainer extends SimpleToken implements IToken {

	@SuppressWarnings("rawtypes")
	private IMessage message;

	public MessageContainer(String id) {
		super(id);
	}

	public MessageContainer() {
		this(MessageContainerNames.generateName());
	}

	public void setMessage(@SuppressWarnings("rawtypes") IMessage message) {
		this.message = message;
	}

	@SuppressWarnings("unchecked")
	public <T> IMessage<T> getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return String.format("%s('%s'){%s}", getClass().getSimpleName(), getId(), getMessage());
	}

}
