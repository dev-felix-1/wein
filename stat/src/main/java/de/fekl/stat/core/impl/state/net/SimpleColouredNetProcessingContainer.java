package de.fekl.stat.core.impl.state.net;

import java.util.List;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IProcessFinishedEvent;
import de.fekl.stat.core.api.node.IAutoMergeNode;
import de.fekl.stat.core.api.node.IAutoSplitNode;
import de.fekl.stat.core.api.state.IStateContainer;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.events.EndNodeReachedEvent;
import de.fekl.stat.core.impl.events.ProcessFinishedEvent;
import de.fekl.stat.core.impl.events.ProcessStartedEvent;
import de.fekl.stat.core.impl.events.SimpleEventBus;
import de.fekl.stat.core.impl.events.StepFinishedEvent;
import de.fekl.stat.core.impl.events.StepStartedEvent;
import de.fekl.stat.core.impl.state.SimpleStateContainer;
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

	public SimpleColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		this.net = net;
		this.tokenFactory = tokenFactory;
		setRunning(false);
		stepCounter = 0;
		stateChangedEventBus = new SimpleEventBus<>();
		processingEventBus = new SimpleEventBus<>();
		stateContainer = new SimpleStateContainer<>(initialState, stateChangedEventBus);
	}

	public SimpleColouredNetProcessingContainer(ISpongeNet<N> net, ITokenFactory<T> tokenFactory) {
		this(net, new SimpleTokenStore<>(), tokenFactory);
	}

	public void process(T token) {
		if (running) {
			throw new IllegalStateException("There is already a process running on this container");
		}
		startProcessing();
		stateContainer.changeState(ColouredNetOperations.putToken(net.getRootId(), token));
		step();
	}

	protected void startProcessing() {
		setRunning(true);
		processingEventBus.post(new ProcessStartedEvent());
	}

	protected void setRunning(boolean running) {
		this.running = running;
	}

	protected boolean isFinished() {
		return stateContainer.getCurrentState().getTokenPositions().entrySet().stream()
				.allMatch(e -> net.isLeaf(e.getValue()));
	}

	private void step() {
		boolean finished = isFinished();

		processingEventBus.post(new StepStartedEvent(stepCounter++));

		for (var entry : stateContainer.getCurrentState().getTokenPositions().entrySet()) {
			String nodeId = entry.getValue();
			String tokenId = entry.getKey();
			T token = stateContainer.getCurrentState().getToken(tokenId);
			if (token != null) {
				N node = net.getNode(nodeId);
				handleTokenTransition(stateContainer, token, node);
			}
		}

		processingEventBus.post(new StepFinishedEvent(stepCounter));

		if (finished) {
			setRunning(false);
			processingEventBus.post(new ProcessFinishedEvent());
		} else {
			step();
		}
	}

	protected boolean isSplitterNode(N node) {
		return node instanceof IAutoSplitNode;
	}

	protected boolean isMergerNode(N node) {
		return node instanceof IAutoMergeNode;
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleTokenSplitTransition(C stateContainer, T token,
			N currentNode, List<IEdge> outgoingEdges) {
		for (int i = 0; i < outgoingEdges.size(); i++) {
			IEdge edge = outgoingEdges.get(i);
			if (i == outgoingEdges.size() - 1) {
				handleTokenMoveTransition(stateContainer, token, edge);
			} else {
				handleTokenCopyTransition(stateContainer, token, edge);
			}
		}
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleTokenTransition(C stateContainer, T token,
			N currentNode) {
		List<IEdge> outgoingEdges = net.getOutgoingEdges(currentNode);
		if (!outgoingEdges.isEmpty()) {
			if (isSplitterNode(currentNode)) {
				handleTokenSplitTransition(stateContainer, token, currentNode, outgoingEdges);
			} else {
				IEdge firstEdge = outgoingEdges.get(0);
				handleTokenMoveTransition(stateContainer, token, firstEdge);
			}
		} else {
			processingEventBus.post(new EndNodeReachedEvent<>(currentNode, token));
		}
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleTokenCopyTransition(C stateContainer, T token,
			IEdge edge) {
		String target = edge.getTarget();
		stateContainer.changeState(ColouredNetOperations.copyToken(target, token, tokenFactory));
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleTokenMoveTransition(C stateContainer, T token,
			IEdge edge) {
		String target = edge.getTarget();
		String source = edge.getSource();
		stateContainer.changeState(ColouredNetOperations.moveToken(source, target, token));
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

	protected void onProcessingEvent(IEventListener<IEvent> listener) {
		processingEventBus.register(listener);
	}

	protected <E extends IEvent> void onProcessingEvent(Class<E> type, IEventListener<E> listener) {
		processingEventBus.register(type, listener);
	}

	protected <E extends IEvent> void onProcessingFinished(IEventListener<IProcessFinishedEvent> listener) {
		processingEventBus.register(IProcessFinishedEvent.class, listener);
	}

	protected void onStateChangeEvent(IEventListener<IEvent> listener) {
		stateChangedEventBus.register(listener);
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

}
