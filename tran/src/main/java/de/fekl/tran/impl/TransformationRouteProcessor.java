package de.fekl.tran.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import de.fekl.stat.core.impl.state.net.SimpleColouredNetProcessor;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformationRoute;

public class TransformationRouteProcessor extends SimpleColouredNetProcessor {

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
		return processingContainer.getAllCurrentlyProcessed().get(0).getMessage();
	}

	@SuppressWarnings("unchecked")
	protected <T> List<IMessage<T>> processForMultiResult(MessageContainer messageContainer,
			TransformationNetProcessingContainer processingContainer) throws InterruptedException {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> processingContainer.process(messageContainer));

		var result = (List<IMessage<T>>) (List<?>) processingContainer.getAllCurrentlyProcessed().stream()
				.map(mc -> mc.getMessage()).collect(Collectors.toList());

		executor.shutdown();
		return result;

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

	public <S, T> List<IMessage<T>> processForMultiResult(IMessage<S> message, ITransformationRoute<S, T> route)
			throws InterruptedException {
		MessageContainer messageContainer = wrapMessage(message);
		TransformationNetProcessingContainer processingContainer = createProcessingContainer(route);
		return processForMultiResult(messageContainer, processingContainer);
	}

	public <S, T> List<IMessage<T>> processForMultiResult(S messageContent, ITransformationRoute<S, T> route)
			throws InterruptedException {
		IMessage<S> message = wrapMessageContent(messageContent);
		return processForMultiResult(message, route);
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
