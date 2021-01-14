package de.fekl.tran.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformationRoute;

public class TransformationRouteProcessor {

	protected <T> MessageContainer wrapMessage(IMessage<T> message) {
		MessageContainer messageContainer = new MessageContainer();
		messageContainer.setMessage(message);
		return messageContainer;
	}

	protected <T> IMessage<T> wrapMessageContent(T messageContent) {
		return new SimpleMessageFactory().createMessage(messageContent);
	}

	protected <S, T> TransformationNetProcessingContainer createProcessingContainer(ITransformationRoute<S, T> route) {
		return new TransformationNetProcessingContainer(route.getGraph());
	}

	protected <T> IMessage<T> process(MessageContainer messageContainer,
			TransformationNetProcessingContainer processingContainer) throws InterruptedException {
		processingContainer.process(messageContainer);
		MessageContainer processedMessageContainer = processingContainer.getNextProcessed();
		return processedMessageContainer.getMessage();
	}

	// -- sync - blocking
	public <S, T> IMessage<T> process(IMessage<S> message, ITransformationRoute<S, T> route)
			throws InterruptedException {
		MessageContainer messageContainer = wrapMessage(message);
		TransformationNetProcessingContainer processingContainer = createProcessingContainer(route);
		return process(messageContainer, processingContainer);
	}

	public <S, T> IMessage<T> process(S messageContent, ITransformationRoute<S, T> route) throws InterruptedException {
		IMessage<S> message = wrapMessageContent(messageContent);
		return process(message, route);
	}

	// -- async - nonblocking
	public <S, T> Future<IMessage<T>> processAsync(IMessage<S> message, ITransformationRoute<S, T> route) {
		MessageContainer messageContainer = wrapMessage(message);
		TransformationNetProcessingContainer processingContainer = createProcessingContainer(route);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		return executor.submit(() -> process(messageContainer, processingContainer));
	}

	public <S, T> Future<IMessage<T>> processAsync(S messageContent, ITransformationRoute<S, T> route) {
		IMessage<S> message = wrapMessageContent(messageContent);
		return processAsync(message, route);
	}

}
