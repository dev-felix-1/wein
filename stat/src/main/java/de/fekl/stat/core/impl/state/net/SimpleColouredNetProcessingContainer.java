package de.fekl.stat.core.impl.state.net;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.edge.conditional.ICondition;
import de.fekl.stat.core.api.edge.conditional.IConditionalEdge;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IStepFinishedEvent;
import de.fekl.stat.core.api.events.IStepStartedEvent;
import de.fekl.stat.core.api.node.IAutoMergeNode;
import de.fekl.stat.core.api.node.IAutoSplitNode;
import de.fekl.stat.core.api.state.IHistory;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.events.EndNodeReachedEvent;
import de.fekl.stat.core.impl.events.ProcessFinishedEvent;
import de.fekl.stat.core.impl.events.ProcessStartedEvent;
import de.fekl.stat.core.impl.events.ProcessWaitingEvent;
import de.fekl.stat.core.impl.events.SimpleEventBus;
import de.fekl.stat.core.impl.events.StepFinishedEvent;
import de.fekl.stat.core.impl.events.StepStartedEvent;
import de.fekl.stat.core.impl.state.TokenStoreStateContainer;
import de.fekl.stat.core.impl.token.SimpleTokenStore;

public class SimpleColouredNetProcessingContainer<N extends INode, T extends IToken>
		extends AbstractColouredNetProcessingContainer<T, N> implements IColouredNetProcessingContainer<T> {

	private boolean running;
	private long stepCounter;
	private CountDownLatch waitForUpdate;

	private final List<T> waitingTokens = new ArrayList<>();
	private final Map<String, Map<String, List<T>>> tokenBuffers = new HashMap<>();

	public SimpleColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		super(net, new TokenStoreStateContainer<>(initialState), tokenFactory, new SimpleEventBus<>(),
				new SimpleEventBus<>());
		setRunning(false);
		stepCounter = 0;
	}

	public SimpleColouredNetProcessingContainer(ISpongeNet<N> net, ITokenFactory<T> tokenFactory) {
		this(net, new SimpleTokenStore<>(), tokenFactory);
	}

	public void process(T token) {
		if (isRunning()) {
			throw new IllegalStateException("There is already a process running on this container");
		}
		startProcessing();
		changeState(ColouredNetOperations.putToken(getNet().getRootId(), token));
		keepRunning();
	}

	protected void startProcessing() {
		setRunning(true);
		postProcessingEvent(new ProcessStartedEvent());
	}

	protected void setRunning(boolean running) {
		this.running = running;
	}

	protected List<T> getUnfinishedTokens() {
		return getTokenPositions().entrySet().stream().filter(e -> !getNet().isLeaf(e.getValue()))
				.map(e -> getCurrentState().getToken(e.getKey())).collect(Collectors.toList());
	}

	protected Map<String, String> getTokenPositions() {
		return getCurrentState().getTokenPositions();
	}

	private void keepRunning() {
		boolean finished = false;
		do {
			finished = isFinished();
			step();
			waitIfNecessary();
		} while (!finished);
		shutdownProcess();
	}

	private void step() {
		postProcessingEvent(new StepStartedEvent(stepCounter++));
		handleTransitions();
		handleMerges();
		postProcessingEvent(new StepFinishedEvent(stepCounter));
	}

	protected void waitIfNecessary() {
		if (waitingTokens.size() > 0) {
			List<T> relevantTokens = getUnfinishedTokens();
			if (relevantTokens.size() == waitingTokens.size()) {
				waitForUpdate();
			}
		}
	}

	private void waitForUpdate() {
		try {
			postProcessingEvent(new ProcessWaitingEvent());
			waitForUpdate.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	protected void shutdownProcess() {
		setRunning(false);
		postProcessingEvent(new ProcessFinishedEvent());
	}

	protected void handleMerges() {
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

	protected void handleTokenMerge(List<T> tokens, String targetNode) {
		changeState(ColouredNetOperations.mergeToken(targetNode, tokens, getTokenFactory()));
	}

	protected void handleTransitions() {
		for (var entry : getTokenPositions().entrySet()) {
			String nodeId = entry.getValue();
			String tokenId = entry.getKey();
			T token = getCurrentState().getToken(tokenId);
			if (token != null) {
				N node = getNet().getNode(nodeId);
				handleTokenTransition(token, node);
			}
		}
	}

	protected boolean isSplitterNode(N node) {
		return node instanceof IAutoSplitNode;
	}

	protected boolean isMergerNode(N node) {
		return node instanceof IAutoMergeNode;
	}

	protected void handleTokenSplitTransition(T token, N currentNode, List<IEdge> outgoingEdges) {
		for (int i = 0; i < outgoingEdges.size(); i++) {
			IEdge edge = outgoingEdges.get(i);
			if (edge instanceof IConditionalEdge) {
				throw new IllegalStateException("Conditional Edge on Splitter-Node currently not supported!");
			}
			if (i == outgoingEdges.size() - 1) {
				handleTokenMoveTransition(token, edge);
			} else {
				handleTokenCopyTransition(token, edge);
			}
		}
	}

	protected void handleTokenTransition(T token, N currentNode) {
		List<IEdge> outgoingEdges = getNet().getOutgoingEdges(currentNode);
		if (!outgoingEdges.isEmpty()) {
			if (isSplitterNode(currentNode)) {
				handleTokenSplitTransition(token, currentNode, outgoingEdges);
			} else if (isMergerNode(currentNode)) {
				handleTokenWaitingForMerge(token, currentNode, outgoingEdges);
			} else {
				Optional<IEdge> firstEdge = outgoingEdges.stream().filter(e -> edgeCanTransition(token, e)).findFirst();
				if (firstEdge.isPresent()) {
					handleTokenMoveTransition(token, firstEdge.get());
				} else {
					waitingTokens.add(token);
					waitForUpdate = new CountDownLatch(1);
				}
			}
		} else {
			postProcessingEvent(new EndNodeReachedEvent<>(currentNode, token));
		}
	}

	private void handleTokenWaitingForMerge(T token, N currentNode, List<IEdge> outgoingEdges) {
		List<T> allBufferedTokens = tokenBuffers.computeIfAbsent(currentNode.getId(), s -> new HashMap<>()).values()
				.stream().flatMap(Collection::stream).collect(Collectors.toList());
		if (!allBufferedTokens.contains(token)) {
			Optional<IEdge> firstEdge = outgoingEdges.stream().filter(e -> edgeCanTransition(token, e)).findFirst();
			if (firstEdge.isPresent()) {
				handleTokenMoveTransition(token, firstEdge.get());
			} else {
				waitingTokens.add(token);
				waitForUpdate = new CountDownLatch(1);
			}
		}
	}

	protected void handleTokenCopyTransition(T token, IEdge edge) {
		if (edgeCanTransition(token, edge)) {
			String target = edge.getTarget();
			changeState(ColouredNetOperations.copyToken(target, token, getTokenFactory()));
		} else {
			waitingTokens.add(token);
			waitForUpdate = new CountDownLatch(1);
		}
	}

	protected boolean edgeCanTransition(T token, IEdge edge) {
		if (edge instanceof IConditionalEdge<?, ?>conditionalEdge) {
			String target = edge.getTarget();
			String source = edge.getSource();
			N sourceNode = getNet().getNode(source);
			N targetNode = getNet().getNode(target);
			@SuppressWarnings("unchecked")
			ICondition<N, T> condition = ((IConditionalEdge<N, T>) conditionalEdge).getCondition();
			return condition.evaluate(sourceNode, targetNode, token);
		} else {
			return true;
		}
	}

	protected void handleTokenMoveTransition(T token, IEdge edge) {
		String target = edge.getTarget();
		String source = edge.getSource();
		changeState(ColouredNetOperations.moveToken(source, target, token));
		if (isMergerNode(getNet().getNode(target))) {
			handleTokenArrivedOnMergeNode(source, target, token);
		}

	}

	protected void handleTokenArrivedOnMergeNode(String sourceNodeId, String mergerNodeId, T token) {
		List<T> bufferedTokens = tokenBuffers.computeIfAbsent(mergerNodeId, s -> new HashMap<>())
				.computeIfAbsent(sourceNodeId, s -> new LinkedList<>());
		bufferedTokens.add(token);
	}

	protected List<N> getMergeNodes() {
		return getNet().getNodes().stream().filter(this::isMergerNode).collect(Collectors.toList());
	}

	@Override
	public void reset() {
		getStateContainer().reset();
		setRunning(false);
		stepCounter = 0;
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
		if (waitForUpdate != null) {
			waitForUpdate.countDown();
		}
	}

	@Override
	public boolean isWaiting() {
		return waitForUpdate != null && waitForUpdate.getCount() > 0;
	}

	public void onStepStart(IEventListener<IStepStartedEvent> listener) {
		onProcessingEvent(IStepStartedEvent.class, listener);
	}

	public void onStepFinish(IEventListener<IStepFinishedEvent> listener) {
		onProcessingEvent(IStepFinishedEvent.class, listener);
	}

}
