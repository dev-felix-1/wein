package de.fekl.tran;

import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.SimpleToken;

public class MessageContainer extends SimpleToken implements IToken {

	public MessageContainer(String id) {
		super(id);
	}

	private IMessage message;

	public void setMessage(IMessage message) {
		this.message = message;
	}

	public IMessage getMessage() {
		return message;
	}

}
