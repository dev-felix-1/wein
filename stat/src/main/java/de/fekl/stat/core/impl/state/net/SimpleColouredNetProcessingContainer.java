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
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IProcessFinishedEvent;
import de.fekl.stat.core.api.events.IProcessStartedEvent;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.events.IStepFinishedEvent;
import de.fekl.stat.core.api.events.IStepStartedEvent;
import de.fekl.stat.core.api.events.ITokenCreationEvent;
import de.fekl.stat.core.api.events.ITokenMergeEvent;
import de.fekl.stat.core.api.events.ITokenTransitionEvent;
import de.fekl.stat.core.api.node.IAutoMergeNode;
import de.fekl.stat.core.api.node.IAutoSplitNode;
import de.fekl.stat.core.api.state.IHistory;
import de.fekl.stat.core.api.state.IStateContainer;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
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
		implements IColouredNetProcessingContainer<T> {

	private final IStateContainer<ITokenStore<T>> stateContainer;
	private final ISpongeNet<N> net;
	private final IEventBus<IEvent> stateChangedEventBus;
	private final IEventBus<IEvent> processingEventBus;

	private ITokenFactory<T> tokenFactory;
	private boolean running;
	private long stepCounter;
	private CountDownLatch waitForUpdate;

	private final List<T> waitingTokens = new ArrayList<>();
	private final Map<String, Map<String, List<T>>> tokenBuffers = new HashMap<>();

	public SimpleColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		this.net = net;
		this.tokenFactory = tokenFactory;
		setRunning(false);
		stepCounter = 0;
		stateChangedEventBus = new SimpleEventBus<>();
		processingEventBus = new SimpleEventBus<>();
		stateContainer = new TokenStoreStateContainer<>(initialState, stateChangedEventBus);
	}

	public SimpleColouredNetProcessingContainer(ISpongeNet<N> net, ITokenFactory<T> tokenFactory) {
		this(net, new SimpleTokenStore<>(), tokenFactory);
	}

	public void process(T token) {
		if (isRunning()) {
			throw new IllegalStateException("There is already a process running on this container");
		}
		startProcessing();
		changeState(ColouredNetOperations.putToken(net.getRootId(), token));
		keepRunning();
	}

	protected void startProcessing() {
		setRunning(true);
		postProcessingEvent(new ProcessStartedEvent());
	}

	protected void setRunning(boolean running) {
		this.running = running;
	}

	protected boolean isFinished() {
		return getTokenPositions().entrySet().stream().allMatch(e -> net.isLeaf(e.getValue()));
	}

	protected List<T> getUnfinishedTokens() {
		return getTokenPositions().entrySet().stream().filter(e -> !net.isLeaf(e.getValue()))
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
		stateContainer.changeState(ColouredNetOperations.moveToken(source, target, token));
		if (isMergerNode(getNet().getNode(target))) {
			handleTokenArrivedOnMergeNode(source, target, token);
		}

	}

	protected void handleTokenArrivedOnMergeNode(String sourceNodeId, String mergerNodeId, T token) {
		List<T> bufferedTokens = tokenBuffers.computeIfAbsent(mergerNodeId, s -> new HashMap<>())
				.computeIfAbsent(sourceNodeId, s -> new LinkedList<>());
		bufferedTokens.add(token);
	}

	protected ISpongeNet<N> getNet() {
		return net;
	}

	protected IStateContainer<ITokenStore<T>> getStateContainer() {
		return stateContainer;
	}

	protected ITokenFactory<T> getTokenFactory() {
		return tokenFactory;
	}

	protected List<N> getMergeNodes() {
		return getNet().getNodes().stream().filter(this::isMergerNode).collect(Collectors.toList());
	}

	@Override
	public void onProcessingEvent(IEventListener<IEvent> listener) {
		processingEventBus.register(listener);
	}

	protected <E extends IEvent> void onProcessingEvent(Class<E> type, IEventListener<E> listener) {
		processingEventBus.register(type, listener);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onStateChangedEvent(
			IEventListener<IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>> listener) {
		stateChangedEventBus.register(
				(Class<IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>>) (Class<?>) IStateHasChangedEvent.class,
				listener);
	}

	protected <E extends IEvent> void onStateChangeEvent(Class<E> type, IEventListener<E> listener) {
		stateChangedEventBus.register(type, listener);
	}

	@Override
	public ITokenStore<T> getCurrentState() {
		return stateContainer.getCurrentState();
	}

	@Override
	public void reset() {
		stateContainer.reset();
		setRunning(false);
		stepCounter = 0;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void onFinish(IEventListener<IProcessFinishedEvent> listener) {
		processingEventBus.register(IProcessFinishedEvent.class, listener);

	}

	@Override
	public void onStart(IEventListener<IProcessStartedEvent> listener) {
		processingEventBus.register(IProcessStartedEvent.class, listener);
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

	protected void changeState(IStateChangeOperation<ITokenStore<T>> stateChangeOperation) {
		stateContainer.changeState(stateChangeOperation);
	}

	protected void postProcessingEvent(IEvent event) {
		processingEventBus.post(event);
	}

	public void onStepStart(IEventListener<IStepStartedEvent> listener) {
		processingEventBus.register(IStepStartedEvent.class, listener);

	}

	public void onStepFinish(IEventListener<IStepFinishedEvent> listener) {
		processingEventBus.register(IStepFinishedEvent.class, listener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onTokenCreation(IEventListener<ITokenCreationEvent<T>> listener) {
		stateChangedEventBus.register(ITokenCreationEvent.class,
				(IEventListener<ITokenCreationEvent>) (IEventListener<?>) listener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onTokenTransition(IEventListener<ITokenTransitionEvent<T>> listener) {
		stateChangedEventBus.register(ITokenTransitionEvent.class,
				(IEventListener<ITokenTransitionEvent>) (IEventListener<?>) listener);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onTokenMerge(IEventListener<ITokenMergeEvent<T>> listener) {
		stateChangedEventBus.register(ITokenMergeEvent.class,
				(IEventListener<ITokenMergeEvent>) (IEventListener<?>) listener);

	}

}
