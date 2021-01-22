package de.fekl.tran.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.events.IEndNodeReachedEvent;
import de.fekl.stat.core.api.events.IProcessFinishedEvent;
import de.fekl.stat.core.api.events.IProcessStartedEvent;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.events.IStepStartedEvent;
import de.fekl.stat.core.api.state.IStateChangeOperation;
import de.fekl.stat.core.api.state.IStateContainer;
import de.fekl.stat.core.api.state.net.ITokenCreationOperation;
import de.fekl.stat.core.api.state.net.ITokenMergeOperation;
import de.fekl.stat.core.api.state.net.ITokenTransitionOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.state.net.ColouredNetOperations;
import de.fekl.stat.core.impl.state.net.SimpleColouredNetProcessingContainer;
import de.fekl.stat.core.impl.token.SimpleTokenStore;
import de.fekl.stat.util.ILogger;
import de.fekl.stat.util.LogManager;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.IMessageContainer;
import de.fekl.tran.api.core.ITransformer;

public class TransformationNetProcessingContainer
		extends SimpleColouredNetProcessingContainer<ITransformer<?, ?>, MessageContainer> {

	private static final ILogger LOG = LogManager.getInstance().getLogger(TransformationNetProcessingContainer.class);

	// tokenId to Edge
	private final Map<String, IEdge> tokensAwaitingMerge = new HashMap<>();

	private final BlockingQueue<MessageContainer> processedTokens = new ArrayBlockingQueue<>(64);

	private volatile CountDownLatch waitForStart = new CountDownLatch(1);
	private volatile CountDownLatch waitForFinish = new CountDownLatch(1);

	public TransformationNetProcessingContainer(ISpongeNet<ITransformer<?, ?>> net,
			ITokenStore<MessageContainer> initialState) {
		super(net, initialState, new MessageContainerFactory());

		onStateChangeEvent(event -> {
			if (event instanceof IStateHasChangedEvent<?>stateChangedEvent) {
				if (stateChangedEvent.getSourceOperation() instanceof ITokenTransitionOperation<?>tokenTransition) {

					IToken transitionedToken = tokenTransition.getTransitionedToken();
					String targetNodeId = tokenTransition.getTargetNodeId();
					ITransformer<?, ?> transformer = getNet().getNode(targetNodeId);
					if (transformer instanceof IMerger) {
						// skip
					} else {
						handleTransformation(transformer, (IMessageContainer) transitionedToken);
					}
				} else if (stateChangedEvent.getSourceOperation() instanceof ITokenCreationOperation<?>tokenCreation) {

					IToken transitionedToken = tokenCreation.getCreatedToken();
					String targetNodeId = tokenCreation.getTargetNodeId();
					ITransformer<?, ?> transformer = getNet().getNode(targetNodeId);
					handleTransformation(transformer, (IMessageContainer) transitionedToken);
				}else if (stateChangedEvent.getSourceOperation() instanceof ITokenMergeOperation<?>tokenMerge) {

					List<MessageContainer> mergedToken = (List<MessageContainer>) tokenMerge.mergedTokens();
					IToken resultToken = tokenMerge.getResultToken();
					String targetNodeId = tokenMerge.getTargetNodeId();
					IMerger<?> merger = (IMerger<?>) getNet().getNode(targetNodeId);
					handleTransformation(merger, (IMessageContainer) resultToken);
				}
			}
		});

		onProcessingEvent(event -> {
			if (event instanceof IEndNodeReachedEvent endNodeReachedEvent) {
				MessageContainer token = (MessageContainer) endNodeReachedEvent.getToken();
				processedTokens.add(token);
				IStateChangeOperation<ITokenStore<MessageContainer>> removeTokenOp = ColouredNetOperations
						.removeToken(endNodeReachedEvent.getNode().getId(), token);
				getStateContainer().changeState(removeTokenOp);
			}
		});

		onProcessingEvent(event -> {
			if (event instanceof IStepStartedEvent) {
				LOG.debug("Processing container %s, step %s with state: %s", "this",
						((IStepStartedEvent) event).getStep(),
						ITokenStore.print(getStateContainer().getCurrentState()));

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
	protected <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleTokenTransition(C stateContainer,
			MessageContainer token, ITransformer<?, ?> node) {
		if (tokensAwaitingMerge.containsKey(token.getId())) {
			// skip
		} else {
			super.handleTokenTransition(stateContainer, token, node);
		}
	}

	protected boolean isSplitterNode(ITransformer<?, ?> node) {
		return node.isAutoSplit();
	}

	@Override
	protected <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleTokenCopyTransition(
			C stateContainer, MessageContainer token, IEdge edge) {
		String targetNodeId = edge.getTarget();
		ITransformer<?, ?> targetNode = getNet().getNode(targetNodeId);
		if (targetNode instanceof IMerger) {
			handleTokenMerge(stateContainer, edge, targetNodeId);
		} else {
			super.handleTokenCopyTransition(stateContainer, token, edge);
		}
	}

	private <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleTokenMerge(C stateContainer,
			IEdge edge, String targetNodeId) {
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
			
			List<Entry<String, IEdge>> collect = tokensAwaitingMerge.entrySet().stream()
					.filter(e -> incomingEdges.contains(e.getValue()))
					.collect(Collectors.toList());
			
			List<MessageContainer> tokensToMerge = new ArrayList<>();
			for (var entry : collect) {
				String tokenId = entry.getKey();
				MessageContainer token = stateContainer.getCurrentState().getToken(tokenId);
				tokensToMerge.add(token);
				super.handleTokenMoveTransition(stateContainer, token, entry.getValue());
				tokensAwaitingMerge.remove(entry.getKey());
			}
			ITokenMergeOperation<MessageContainer> mergeToken = ColouredNetOperations.mergeToken(targetNodeId,
					tokensToMerge, getTokenFactory());
			stateContainer.changeState(mergeToken);
		}
	}

	@Override
	protected <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleTokenMoveTransition(
			C stateContainer, MessageContainer token, IEdge edge) {
		String targetNodeId = edge.getTarget();
		ITransformer<?, ?> targetNode = getNet().getNode(targetNodeId);
		if (targetNode instanceof IMerger) {
			handleTokenMerge(stateContainer, edge, targetNodeId);
		} else {
			super.handleTokenMoveTransition(stateContainer, token, edge);
		}
	}

	protected void handleTransformation(ITransformer<?, ?> node, IMessageContainer messageContainer) {
		IMessage<?> transformedMessage = node.transform(messageContainer.getMessage());
		((MessageContainer) messageContainer).setMessage(transformedMessage);
	}

	public MessageContainer getNextProcessed() throws InterruptedException, TimeoutException {
		MessageContainer poll = processedTokens.poll(3, TimeUnit.SECONDS);
		if (poll == null) {
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
