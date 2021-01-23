package de.fekl.stat.core.impl.state.net;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.util.RandomNames;
import de.fekl.stat.core.api.edge.conditional.IConditionalEdge;
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IEventQueue;
import de.fekl.stat.core.api.events.IProcessFinishedEvent;
import de.fekl.stat.core.api.events.IProcessStartedEvent;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.IStateContainer;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingCondition;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.events.EndNodeReachedEvent;
import de.fekl.stat.core.impl.events.ProcessAbortedEvent;
import de.fekl.stat.core.impl.events.ProcessFinishedEvent;
import de.fekl.stat.core.impl.events.ProcessStartedEvent;
import de.fekl.stat.core.impl.events.SimpleAsyncEventBus;
import de.fekl.stat.core.impl.events.SimpleEventQueue;
import de.fekl.stat.core.impl.state.SimpleStateContainer;
import de.fekl.stat.util.ILogger;
import de.fekl.stat.util.LogManager;

@Deprecated
public class AsyncColouredNetProcessingContainer<N extends INode, T extends IToken>
		implements IColouredNetProcessingContainer<T> {

	private static final ILogger LOG = LogManager.getInstance()
			.getLogger(AsyncColouredNetProcessingContainer.class);

	private final IStateContainer<ITokenStore<T>> stateContainer;
	private final ISpongeNet<N> net;

	private final String processingContainerId;

	private ITokenFactory<T> tokenFactory;
	private boolean running;
	private boolean abort;
	private long stepCounter;

	private final SimpleAsyncEventBus<IEvent> stateChanedEventBus;
	private final SimpleAsyncEventBus<IEvent> processingEventBus;

	private final IEventQueue<IEvent> stateChangedEvents;

	private boolean stateChanedEventBusDaemon = false;
	private boolean processingEventBusDaemon = false;

	public AsyncColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		this.net = net;
		this.tokenFactory = tokenFactory;
		processingContainerId = RandomNames.getRandomName(AsyncColouredNetProcessingContainer.class.getName(),
				"processor_", 15);
		running = false;
		abort = false;
		stepCounter = 0;
		// FIXME find all paths from start to end and set it like this
		stateChangedEvents = new SimpleEventQueue<>(net.getNodes().size() * 6);
		if (stateChanedEventBusDaemon) {
			stateChanedEventBus = new SimpleAsyncEventBus<>(net.getNodes().size() * 2);
		} else {
			stateChanedEventBus = new SimpleAsyncEventBus<>(1);
		}
		if (processingEventBusDaemon) {
			processingEventBus = new SimpleAsyncEventBus<>(net.getLeafs().size() * 2);
		} else {
			processingEventBus = new SimpleAsyncEventBus<>(1);
		}
		stateChanedEventBus.register(event -> stateChangedEvents.add(event));
		stateContainer = new SimpleStateContainer<>(initialState, stateChanedEventBus);
	}

	public void process(T token) {
		String startNodeId = net.getRoot().getId();
		if (stateChanedEventBusDaemon) {
			new Thread(stateChanedEventBus).start();
		}
		if (processingEventBusDaemon) {
			new Thread(processingEventBus).start();
		}
		stateContainer.changeState(ColouredNetOperations.putToken(startNodeId, token));
		run();
	}

	protected void setRunning(boolean running) {
		this.running = running;
	}

	public void abort() {
		abort = true;
		processingEventBus.post(new ProcessAbortedEvent());
	}

	public void run() {
		setRunning(true);
		processingEventBus.post(new ProcessStartedEvent());
		stateChanedEventBus.waitForHandlers();
		while (running && !abort) {
			if (running && stateContainer.getCurrentState().getTokenPositions().entrySet().stream()
					.allMatch(e -> net.isLeaf(e.getValue()))) {
				LOG.debug("Stop running because every token is in a final place.");
				setRunning(false);
			}

			try {
				IEvent processingEvent = stateChangedEvents.poll(1, TimeUnit.SECONDS);
				stateChanedEventBus.waitForHandlers();
				if (processingEvent != null) {
					step();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		setRunning(false);
		processingEventBus.post(new ProcessFinishedEvent());
		stateChanedEventBus.waitForHandlers();
		processingEventBus.waitForHandlers();
		shutdown();
	}

	public void shutdown() {
		stateChanedEventBus.abort();
		processingEventBus.abort();
	}

	private synchronized void step() {
		LOG.debug("Processing container %s, step %s with state: %s", processingContainerId, stepCounter++,
				ITokenStore.print(stateContainer.getCurrentState()));
		Map<String, String> tokenToNodeMapping = stateContainer.getCurrentState().getTokenPositions();

		Set<Entry<String, String>> entrySet = Collections.unmodifiableSet(tokenToNodeMapping.entrySet());
		entrySet.forEach(entry -> {

			String nodeId = entry.getValue();
			String tokenId = entry.getKey();
			T token = stateContainer.getCurrentState().getToken(tokenId);
			handleToken(stateContainer, token, nodeId, false);
		});
	}

	protected Set<String> getReachedEndNodes() {
		ITokenStore<T> currentState = stateContainer.getCurrentState();
		Set<String> endNodes = net.getLeafs().stream().map(l -> l.getId()).collect(Collectors.toSet());
		Set<String> reachedEndNodes = currentState.getTokenPositions().values().stream().distinct()
				.filter(endNodes::contains).collect(Collectors.toSet());
		return reachedEndNodes;
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleToken(C stateContainer, T token,
			String sourceNodeId, boolean split) {
		LOG.debug("Handle Token %s on %s ...", token, sourceNodeId);
		List<IEdge> outgoingEdges = net.getOutgoingEdges(sourceNodeId);
		if (!outgoingEdges.isEmpty()) {
			if (split) {
				LOG.debug("Splitting token %s ...", token);
				for (int i = 0; i < outgoingEdges.size(); i++) {
					IEdge edge = outgoingEdges.get(i);
					if (i == outgoingEdges.size() - 1) {
						handleTokenTransition(stateContainer, token, sourceNodeId, edge, false);
					} else {
						handleTokenTransition(stateContainer, token, sourceNodeId, edge, true);
					}
				}
			} else {
				IEdge firstEdge = outgoingEdges.get(0);
				handleTokenTransition(stateContainer, token, sourceNodeId, firstEdge, false);
			}
		} else {
			LOG.debug("Token %s reached an endNode %s.", token, sourceNodeId);
			processingEventBus.post(new EndNodeReachedEvent(null, token));
		}
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleTokenTransition(C stateContainer, T token,
			String sourceNodeId, IEdge edge, boolean copy) {

		boolean shouldTrigger = true;
		if (edge instanceof IConditionalEdge conditionalEdge) {
			if (conditionalEdge.getCondition() instanceof IColouredNetProcessingCondition condition) {
				N sourceNode = getNet().getNode(sourceNodeId);
				N targetNode = getNet().getNode(edge.getTarget());
				shouldTrigger = condition.evaluate(token, sourceNode, targetNode);
			} else {
				LOG.debug("Ignoring unsupported Condition %s of type on edge %s...",
						conditionalEdge.getCondition().getClass().getSimpleName(), edge);
			}
		}

		String targetNodeId = edge.getTarget();
		if (copy) {
			stateContainer.changeState(ColouredNetOperations.copyToken(targetNodeId, token, tokenFactory));
		} else {
			if (shouldTrigger) {
				stateContainer.changeState(ColouredNetOperations.moveToken(sourceNodeId, targetNodeId, token));
			}
		}
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

	public boolean isRunning() {
		return running;
	}

	protected void onProcessingEvent(IEventListener<IEvent> listener) {
		processingEventBus.register(listener);
	}

	protected void onStateChangeEvent(IEventListener<IEvent> listener) {
		stateChanedEventBus.register(listener);
	}

	@Override
	public ITokenStore<T> getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(IEventListener<IProcessFinishedEvent> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(IEventListener<IProcessStartedEvent> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStateChangedEvent(IEventListener<IStateHasChangedEvent<T>> listener) {
		// TODO Auto-generated method stub
		
	}

}
