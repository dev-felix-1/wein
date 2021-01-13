package de.fekl.tran.impl;

import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformationRoute;

public class TransformationRouteProcessor {

	public <S, T> IMessage<T> process(IMessage<S> message, ITransformationRoute<S, T> route)
			throws InterruptedException {
		MessageContainer messageContainer = new MessageContainer(MessageNames.generateMessageName());
		messageContainer.setMessage(message);
		TransformationNetProcessingContainer transformationNetProcessingContainer = new TransformationNetProcessingContainer(
				route.getGraph());
		transformationNetProcessingContainer.process(messageContainer);
		MessageContainer processedMessageContainer = transformationNetProcessingContainer.getNextProcessed();
		return processedMessageContainer.getMessage();
	}

	public <S, T> IMessage<T> process(S messageContent, ITransformationRoute<S, T> route) throws InterruptedException {
		IMessage<S> message = new SimpleMessageFactory().createMessage(messageContent);
		return process(message, route);
	}

}
