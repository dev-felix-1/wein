package de.fekl.tran.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.state.SimpleTokenStore;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.esta.api.core.IStateChangeOperation;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.sepe.ColouredNetOperations;
import de.fekl.sepe.ColouredNetProcessingContainer;
import de.fekl.sepe.IEndNodeReachedEvent;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformer;

public class TransformationNetProcessingContainer
		extends ColouredNetProcessingContainer<ITransformer<?, ?>, MessageContainer> {
	//tokenId to Edge 
	private final Map<String, IEdge> tokensAwaitingMerge = new HashMap<>();

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
	
	@Override
	protected <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleTokenTransition(C stateContainer,
			String tokenId, String sourceNodeId, IEdge edge, boolean copy) {
		
		String targetNodeId = edge.getTarget();
		ITransformer<?, ?> targetNode = getNet().getNode(targetNodeId);
		if (targetNode instanceof IMerger) {
			List<IEdge> incomingEdges = getNet().getIncomingEdges(targetNodeId);
			for(IEdge incomingEdge : incomingEdges) {
				if (tokensAwaitingMerge.values().contains(incomingEdge)) {
					//skip
				} else {
					Set<MessageContainer> tokens = stateContainer.getCurrentState().getTokens(incomingEdge.getSource());
					if(!tokens.isEmpty()) {
						tokensAwaitingMerge.put(tokens.iterator().next().getId(), incomingEdge);
					}
				}
			}
			if (incomingEdges.stream().allMatch(e->tokensAwaitingMerge.values().contains(e))) {
				List<String> collect = tokensAwaitingMerge.entrySet().stream().filter(e->incomingEdges.contains(e.getValue())).map(e->e.getKey()).collect(Collectors.toList());
				stateContainer.changeState(ColouredNetOperations.mergeTokens(getTokenFactory(),targetNodeId,collect));
			}
		} else {
			super.handleTokenTransition(stateContainer, tokenId, sourceNodeId, edge, copy);
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
