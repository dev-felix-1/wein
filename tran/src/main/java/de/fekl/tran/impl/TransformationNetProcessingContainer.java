package de.fekl.tran.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
import de.fekl.sepe.IProcessFinishedEvent;
import de.fekl.sepe.IProcessStartedEvent;
import de.fekl.sepe.ITokenTransitionOperation;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.IMessageContainer;
import de.fekl.tran.api.core.ITransformer;

public class TransformationNetProcessingContainer
		extends ColouredNetProcessingContainer<ITransformer<?, ?>, MessageContainer> {
	// tokenId to Edge
	private final Map<String, IEdge> tokensAwaitingMerge = new HashMap<>();

	private final BlockingQueue<MessageContainer> processedTokens = new ArrayBlockingQueue<>(64);

	private volatile CountDownLatch waitForStart = new CountDownLatch(1);
	private volatile CountDownLatch waitForFinish = new CountDownLatch(1);

	public TransformationNetProcessingContainer(ISpongeNet<ITransformer<?, ?>> net,
			ITokenStore<MessageContainer> initialState) {
		super(net, initialState, new MessageContainerFactory());

		getStateContainer().onStateChangedEvent(event -> {
			if (event.getSourceOperation() instanceof ITokenTransitionOperation<MessageContainer>tokenTransition) {
				tokenTransition.getTransitionedToken().forEach(t -> {
					System.err.println(tokenTransition);
					handleTransformation(getNet().getNode(tokenTransition.getTargetNodeId()), t);
				});
			}
		});

		onProcessingEvent(event -> {
			if (event instanceof IEndNodeReachedEvent endNodeReachedEvent) {
				processedTokens.add(getStateContainer().getCurrentState().getToken(endNodeReachedEvent.getTokenId()));
				IStateChangeOperation<ITokenStore<MessageContainer>> removeTokenOp = ColouredNetOperations
						.removeToken(endNodeReachedEvent.getNodeId(), endNodeReachedEvent.getTokenId());
				getStateContainer().changeState(removeTokenOp);
			}
		});

		onProcessingEvent(event -> {
			if (event instanceof IProcessStartedEvent) {
				waitForStart.countDown();
			}
		});

		onProcessingEvent(event -> {
			if (event instanceof IProcessFinishedEvent) {
				waitForFinish.countDown();
			}
		});
	}

	public TransformationNetProcessingContainer(ISpongeNet<ITransformer<?, ?>> net) {
		this(net, new SimpleTokenStore<>());
	}

	@Override
	protected <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleToken(C stateContainer,
			String tokenId, String sourceNodeId, boolean split) {

		ITransformer<?, ?> node = getNet().getNode(sourceNodeId);
		MessageContainer token = stateContainer.getCurrentState().getToken(tokenId);
		if (token != null) {
			if (!tokensAwaitingMerge.containsKey(tokenId)) {
				//skip
			}
			if (node.isAutoSplit()) {
				super.handleToken(stateContainer, tokenId, sourceNodeId, true);
			} else {
				super.handleToken(stateContainer, tokenId, sourceNodeId, split);
			}
		} else {
			// token was already transitioned while processing the step
		}
	}

	protected void handleTransformation(ITransformer<?, ?> node, IMessageContainer messageContainer) {
		IMessage<?> transformedMessage = node.transform(messageContainer.getMessage());
		((MessageContainer) messageContainer).setMessage(transformedMessage);
	}

	@Override
	protected <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleTokenTransition(C stateContainer,
			String tokenId, String sourceNodeId, IEdge edge, boolean copy) {

		String targetNodeId = edge.getTarget();
		ITransformer<?, ?> targetNode = getNet().getNode(targetNodeId);
		if (targetNode instanceof IMerger) {
			List<IEdge> incomingEdges = getNet().getIncomingEdges(targetNodeId);
			for (IEdge incomingEdge : incomingEdges) {
				if (tokensAwaitingMerge.values().contains(incomingEdge)) {
					// skip
				} else if (incomingEdge.equals(edge)) {
					Set<MessageContainer> tokens = stateContainer.getCurrentState().getTokens(incomingEdge.getSource());
					if (!tokens.isEmpty()) {
						tokensAwaitingMerge.put(tokens.iterator().next().getId(), incomingEdge);
					}
				}
			}
			if (incomingEdges.stream().allMatch(e -> tokensAwaitingMerge.values().contains(e))) {
				List<String> collect = tokensAwaitingMerge.entrySet().stream()
						.filter(e -> incomingEdges.contains(e.getValue())).map(e -> e.getKey())
						.collect(Collectors.toList());
				collect.forEach(t -> tokensAwaitingMerge.remove(t));
				stateContainer.changeState(ColouredNetOperations.mergeTokens(getTokenFactory(), targetNodeId, collect));
			}
		} else {
			super.handleTokenTransition(stateContainer, tokenId, sourceNodeId, edge, copy);
		}
	}

	public MessageContainer getNextProcessed() throws InterruptedException, TimeoutException {
		MessageContainer poll = processedTokens.take();
				//.poll(3, TimeUnit.SECONDS);
		if(poll == null) {
			throw new TimeoutException();
		}
		return poll;
	}

	public List<MessageContainer> getAllCurrentlyProcessed() throws InterruptedException {
		List<MessageContainer> result = new ArrayList<>();
		MessageContainer mc = null;
		while ((mc = processedTokens.poll()) != null) {
			result.add(mc);
		}
		return result;
	}

	public void waitForStart() throws InterruptedException {
		waitForStart.await();
	}

	public void waitForFinish() throws InterruptedException {
		waitForFinish.await();
	}
}
