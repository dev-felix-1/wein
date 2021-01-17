package de.fekl.tran.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.fekl.dine.api.state.ITokenFactory;
import de.fekl.tran.api.core.IAutoMergedMessage;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IMessage;

public class MessageContainerFactory implements ITokenFactory<MessageContainer> {

	@Override
	public MessageContainer createToken(String id) {
		return new MessageContainer(id);
	}

	@Override
	public MessageContainer copyToken(MessageContainer token) {
		IMessage<Object> message = ((MessageContainer) token).getMessage();
		MessageContainer messageContainer = new MessageContainer();
		messageContainer.setMessage(message);
		return messageContainer;
	}

	@Override
	public MessageContainer mergeToken(List<MessageContainer> tokens) {
		List<Object> values = new ArrayList<>(tokens.size());
		List<IContentType<?>> contentTypes = new ArrayList<>(tokens.size());
		for (MessageContainer mc : tokens) {
			IContentType<Object> contentType = mc.getMessage().getContentType();
			Object value = mc.getMessage().getValue();
			values.add(value);
			contentTypes.add(contentType);
		}
		MessageContainer messageContainer = new MessageContainer();
		IAutoMergedMessage mergedMessage = new SimpleAutoMergedMessage(values, contentTypes);
		messageContainer.setMessage(mergedMessage);
		return messageContainer;
	}
}
