package de.fekl.tran.impl;

import java.util.ArrayList;
import java.util.List;

import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.state.SimpleTokenStore;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.sepe.ColouredNetProcessingContainer;
import de.fekl.sepe.IEndNodeReachedEvent;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformer;

public class TransformationNetProcessingContainer
		extends ColouredNetProcessingContainer<ITransformer<?, ?>, MessageContainer> {

	public TransformationNetProcessingContainer(ISpongeNet<ITransformer<?, ?>> net,
			ITokenStore<MessageContainer> initialState) {
		super(net, initialState, new MessageContainerFactory());
	}

	public TransformationNetProcessingContainer(ISpongeNet<ITransformer<?, ?>> net) {
		super(net, new SimpleTokenStore<>(), new MessageContainerFactory());
	}

	@Override
	protected <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleToken(C stateContainer,
			String tokenId, String sourceNodeId, boolean split) {
		ITransformer<?, ?> node = getNet().getNode(sourceNodeId);
		MessageContainer token = stateContainer.getCurrentState().getToken(tokenId);
		IMessage<?> transformedMessage = node.transform(token.getMessage());
		((MessageContainer) token).setMessage(transformedMessage);

		if (node.isAutoSplit()) {
			super.handleToken(stateContainer, tokenId, sourceNodeId, true);
		} else {
			super.handleToken(stateContainer, tokenId, sourceNodeId, split);
		}
	}

	public MessageContainer getNextProcessed() throws InterruptedException {
		IEndNodeReachedEvent endNodeReachedEvent = getEndNodesReachedEvents().take();
		return getStateContainer().getCurrentState().getToken(endNodeReachedEvent.getTokenId());
	}

	public List<MessageContainer> getAllCurrentlyProcessed() throws InterruptedException {
		List<MessageContainer> result = new ArrayList<>();
		IEndNodeReachedEvent nextEvent = null;
		while ((nextEvent = getEndNodesReachedEvents().poll()) != null) {
			MessageContainer mc = getStateContainer().getCurrentState().getToken(nextEvent.getTokenId());
			result.add(mc);
		}
		return result;
	}
	
	public void waitForStart() throws InterruptedException {
		getProcessStartedEvents().take();
	}
	
	public void waitForFinish() throws InterruptedException {
		getProcessFinishedEvents().take();
	}
}
