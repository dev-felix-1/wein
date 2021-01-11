package de.fekl.wein.api.core;

public class TransformationNetProcessor {

	public <T> void process(IMessage<T> message) {
		MessageContainer messageContainer = new MessageContainer(MessageNames.generateMessageName());
		messageContainer.setMessage(message);
	}

}
