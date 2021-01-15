package de.fekl.tran.impl;

import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.ITokenFactory;
import de.fekl.tran.api.core.IMessage;

public class MessageContainerFactory implements ITokenFactory<MessageContainer> {

	@Override
	public MessageContainer createToken(String id) {
		return new MessageContainer(id);
	}

	@Override
	public MessageContainer copyToken(IToken token) {
		IMessage<Object> message = ((MessageContainer) token).getMessage();
		MessageContainer messageContainer = new MessageContainer();
		messageContainer.setMessage(message);
		return messageContainer;
	}

}
