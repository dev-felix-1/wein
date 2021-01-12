package de.fekl.tran;

public class TransformationRouteProcessor {

	public <S, T> IMessage<T> process(IMessage<S> message, ITransformationRoute<S, T> route) {
		MessageContainer messageContainer = new MessageContainer(MessageNames.generateMessageName());
		messageContainer.setMessage(message);
		TransformationNetProcessingContainer transformationNetProcessingContainer = new TransformationNetProcessingContainer(
				route.getGraph());
		transformationNetProcessingContainer.process(messageContainer);
		MessageContainer processedMessageContainer = transformationNetProcessingContainer.getNextProcessed();
		return processedMessageContainer.getMessage();
	}

}
