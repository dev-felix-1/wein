package de.fekl.stat.core.impl.state.net;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.state.IHistory;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.state.operations.ITokenCreationOperation;
import de.fekl.stat.core.api.state.operations.ITokenMergeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.events.EndNodeReachedEvent;
import de.fekl.stat.core.impl.events.ProcessFinishedEvent;
import de.fekl.stat.core.impl.events.ProcessStartedEvent;
import de.fekl.stat.core.impl.events.ProcessWaitingEvent;
import de.fekl.stat.core.impl.events.SimpleEventBus;
import de.fekl.stat.core.impl.state.TokenStoreStateContainer;
import de.fekl.stat.core.impl.token.SimpleTokenStore;

public class AsyncColouredNetProcessingContainer<N extends INode, T extends IToken>
		extends AbstractColouredNetProcessingContainer<T, N> implements IColouredNetProcessingContainer<T> {

	private boolean running;
	private CountDownLatch waitForFinish;

	private final Map<String, Map<String, List<T>>> tokenBuffers = new HashMap<>();

	private final ExecutorService executorService = Executors.newFixedThreadPool(10);
	private final List<TokenProcessingThread> tokenProcessors = new ArrayList<>();

	class TokenProcessingThread implements Runnable {

		private CountDownLatch waitForUpdate;

		private final T token;

		TokenProcessingThread(T token) {
			this.token = token;
		}

		@Override
		public void run() {
			String position;
			do {
				position = getCurrentState().getPosition(token.getId());
				if (position == null) {
					// Token was removed - exit thread
					return;
				}
				N currentNode = getNet().getNode(position);

				handleTokenTransition(currentNode);

			} while (!getNet().isLeaf(position));

		}

		private void waitForUpdate() {
			waitForUpdate = new CountDownLatch(1);
			postProcessingEvent(new ProcessWaitingEvent());
			try {
				waitForUpdate.await();
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
		}

		private void handleTokenSplitTransition(N currentNode, List<IEdge> outgoingEdges) {
			List<IEdge> readyEdges = filterTransitionReadyEdges(outgoingEdges, token);
			for (int i = 0; i < readyEdges.size(); i++) {
				IEdge edge = readyEdges.get(i);
				if (i == readyEdges.size() - 1) {
					handleTokenMoveTransition(token, edge);
				} else {
					handleTokenCopyTransition(token, edge);
				}
			}
		}

		private void handleTokenWaitingForMerge(N currentNode, List<IEdge> outgoingEdges) {
			List<T> allBufferedTokens = tokenBuffers.computeIfAbsent(currentNode.getId(), s -> new HashMap<>()).values()
					.stream().flatMap(Collection::stream).collect(Collectors.toList());
			if (!allBufferedTokens.contains(token)) {
				attemptMoveToken(outgoingEdges);
			}
		}

		private void attemptMoveToken(List<IEdge> outgoingEdges) {
			Optional<IEdge> firstEdge = outgoingEdges.stream().filter(e -> edgeCanTransition(token, e)).findFirst();
			if (firstEdge.isPresent()) {
				handleTokenMoveTransition(token, firstEdge.get());
			} else {
				waitForUpdate();
			}
		}

		private void handleTokenTransition(N currentNode) {
			List<IEdge> outgoingEdges = getNet().getOutgoingEdges(currentNode);
			if (!outgoingEdges.isEmpty()) {
				if (isSplitterNode(currentNode)) {
					handleTokenSplitTransition(currentNode, outgoingEdges);
				} else if (isMergerNode(currentNode)) {
					handleTokenWaitingForMerge(currentNode, outgoingEdges);
					handleMerges();
				} else {
					attemptMoveToken(outgoingEdges);
				}
			} else {
				postProcessingEvent(new EndNodeReachedEvent<>(currentNode, token));
				if (isFinished()) {
					shutdownProcess();
				}
			}
		}

		public void update() {
			if (waitForUpdate != null) {
				waitForUpdate.countDown();
			}
		}

		public boolean isWaiting() {
			return waitForUpdate != null && waitForUpdate.getCount() > 0;
		}

	}

	protected void startTokenProcessing(T token) {
		var tokenProcessor = new TokenProcessingThread(token);
		tokenProcessors.add(tokenProcessor);
		executorService.execute(tokenProcessor);
	}

	public AsyncColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		super(net, new TokenStoreStateContainer<>(initialState), tokenFactory, new SimpleEventBus<>(),
				new SimpleEventBus<>());
		setRunning(false);
	}

	public AsyncColouredNetProcessingContainer(ISpongeNet<N> net, ITokenFactory<T> tokenFactory) {
		this(net, new SimpleTokenStore<>(), tokenFactory);
	}

	public void process(T token) {
		if (isRunning()) {
			throw new IllegalStateException("There is already a process running on this container");
		}
		startProcessing();
		changeState(ColouredNetOperations.putToken(getNet().getRootId(), token));
		startTokenProcessing(token);

		onStateChangedEvent(e -> {
			System.err.println(ITokenStore.print(getCurrentState()));
		});

		try {
			waitForFinish.await();
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
		}

	}

	protected void startProcessing() {
		setRunning(true);
		waitForFinish = new CountDownLatch(1);
		postProcessingEvent(new ProcessStartedEvent());
	}

	protected void setRunning(boolean running) {
		this.running = running;
	}

	private void shutdownProcess() {
		setRunning(false);
		waitForFinish.countDown();
		postProcessingEvent(new ProcessFinishedEvent());
	}

	private synchronized void handleMerges() {
		for (N mergeNode : getMergeNodes()) {
			Map<String, List<T>> tokenBuffer = tokenBuffers.computeIfAbsent(mergeNode.getId(), s -> new HashMap<>());
			List<IEdge> incomingEdges = getNet().getIncomingEdges(mergeNode);
			if (tokenBuffer.size() > 0) {
				boolean doMerge = incomingEdges.size() > 0;
				for (IEdge edge : incomingEdges) {
					List<T> bufferedTokensForSource = tokenBuffer.get(edge.getSource());
					if (bufferedTokensForSource == null || bufferedTokensForSource.isEmpty()) {
						doMerge = false;
						break;
					}
				}
				if (doMerge) {
					List<T> tokensToBeMerged = new ArrayList<>(incomingEdges.size());
					for (IEdge edge : incomingEdges) {
						List<T> bufferedTokensForSource = tokenBuffer.get(edge.getSource());
						tokensToBeMerged.add(bufferedTokensForSource.remove(0));
					}
					handleTokenMerge(tokensToBeMerged, mergeNode.getId());
				}
			}
		}
	}

	private void handleTokenMerge(List<T> tokens, String targetNode) {
		ITokenMergeOperation<T> mergeToken = ColouredNetOperations.mergeToken(targetNode, tokens, getTokenFactory());
		changeState(mergeToken);
		startTokenProcessing(mergeToken.getResultToken());
	}

	private void handleTokenCopyTransition(T token, IEdge edge) {
		String target = edge.getTarget();
		ITokenCreationOperation<T> copyTokenOperation = ColouredNetOperations.copyToken(target, token,
				getTokenFactory());
		changeState(copyTokenOperation);
		startTokenProcessing(copyTokenOperation.getCreatedToken());
	}

	private void handleTokenMoveTransition(T token, IEdge edge) {
		String target = edge.getTarget();
		String source = edge.getSource();
		getStateContainer().changeState(ColouredNetOperations.moveToken(source, target, token));
		if (isMergerNode(getNet().getNode(target))) {
			handleTokenArrivedOnMergeNode(source, target, token);
		}
	}

	private void handleTokenArrivedOnMergeNode(String sourceNodeId, String mergerNodeId, T token) {
		List<T> bufferedTokens = tokenBuffers.computeIfAbsent(mergerNodeId, s -> new HashMap<>())
				.computeIfAbsent(sourceNodeId, s -> new LinkedList<>());
		bufferedTokens.add(token);
	}

	private List<N> getMergeNodes() {
		return getNet().getNodes().stream().filter(this::isMergerNode).collect(Collectors.toList());
	}

	@Override
	public void reset() {
		getStateContainer().reset();
		setRunning(false);
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	public IHistory<ITokenStore<T>> getHistory() {
		return getStateContainer().getHistory();
	}

	@Override
	public void update() {
		tokenProcessors.forEach(p -> {
			if (p != null) {
				p.update();
			}
		});
	}

	@Override
	public boolean isWaiting() {
		return tokenProcessors.stream().anyMatch(p -> p.isWaiting());
	}
}
