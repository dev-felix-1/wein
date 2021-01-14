package de.fekl.tran.impl;

import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.SimpleToken;
import de.fekl.tran.api.core.IMessage;

public class MessageContainer extends SimpleToken implements IToken {

	public MessageContainer(String id) {
		super(id);
	}

	private IMessage message;

	public void setMessage(IMessage message) {
		this.message = message;
	}

	public <T> IMessage<T> getMessage() {
		return message;
	}

}
