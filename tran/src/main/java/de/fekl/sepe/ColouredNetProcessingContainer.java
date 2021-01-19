package de.fekl.sepe;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import de.fekl.baut.ILogger;
import de.fekl.baut.LogManager;
import de.fekl.baut.RandomNames;
import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.edge.conditional.IConditionalEdge;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.ITokenFactory;
import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.state.TokenNames;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.esta.api.core.IEvent;
import de.fekl.esta.api.core.IEventListener;
import de.fekl.esta.api.core.IEventQueue;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.esta.api.core.SimpleEventBus;
import de.fekl.esta.api.core.SimpleEventQueue;
import de.fekl.esta.api.core.SimpleStateContainer;

public class ColouredNetProcessingContainer<N extends INode, T extends IToken> {

	private static final ILogger LOG = LogManager.getInstance().getLogger();

	private final IStateContainer<ITokenStore<T>> stateContainer;
	private final ISpongeNet<N> net;

	private final String processingContainerId;

	private ITokenFactory<T> tokenFactory;
	private boolean running;
	private boolean abort;
	private long stepCounter;

	private final SimpleEventBus<IEvent> stateChanedEventBus;
	private final SimpleEventBus<IEvent> processingEventBus;

	private final IEventQueue<IEvent> stateChangedEvents;

	private boolean stateChanedEventBusDaemon = false;
	private boolean processingEventBusDaemon = false;

	public ColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		this.net = net;
		this.tokenFactory = tokenFactory;
		processingContainerId = RandomNames.getRandomName(ColouredNetProcessingContainer.class.getName(), "processor_",
				15);
		running = false;
		abort = false;
		stepCounter = 0;
		// FIXME find all paths from start to end and set it like this
		stateChangedEvents = new SimpleEventQueue<>(net.getNodes().size()*6);
		stateChanedEventBus = new SimpleEventBus<>(net.getNodes().size()*2);
		processingEventBus = new SimpleEventBus<>(net.getNodes().size());
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
		stateContainer.changeState(ColouredNetOperations.putToken(startNodeId, TokenNames.generateTokenName(), token));
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
			handleToken(stateContainer, tokenId, nodeId, false);
		});
	}

	protected Set<String> getReachedEndNodes() {
		ITokenStore<T> currentState = stateContainer.getCurrentState();
		Set<String> endNodes = net.getLeafs().stream().map(l -> l.getId()).collect(Collectors.toSet());
		Set<String> reachedEndNodes = currentState.getTokenPositions().values().stream().distinct()
				.filter(endNodes::contains).collect(Collectors.toSet());
		return reachedEndNodes;
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleToken(C stateContainer, String tokenId,
			String sourceNodeId, boolean split) {
		LOG.debug("Handle Token %s on %s ...", tokenId, sourceNodeId);
		List<IEdge> outgoingEdges = net.getOutgoingEdges(sourceNodeId);
		if (!outgoingEdges.isEmpty()) {
			if (split) {
				LOG.debug("Splitting token %s ...", tokenId);
				for (int i = 0; i < outgoingEdges.size(); i++) {
					IEdge edge = outgoingEdges.get(i);
					if (i == outgoingEdges.size() - 1) {
						handleTokenTransition(stateContainer, tokenId, sourceNodeId, edge, false);
					} else {
						handleTokenTransition(stateContainer, tokenId, sourceNodeId, edge, true);
					}
				}
			} else {
				IEdge firstEdge = outgoingEdges.get(0);
				handleTokenTransition(stateContainer, tokenId, sourceNodeId, firstEdge, false);
			}
		} else {
			LOG.debug("Token %s reached an endNode %s.", tokenId, sourceNodeId);
			processingEventBus.post(new EndNodeReachedEvent(sourceNodeId, tokenId));
		}
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleTokenTransition(C stateContainer, String tokenId,
			String sourceNodeId, IEdge edge, boolean copy) {

		boolean shouldTrigger = true;
		if (edge instanceof IConditionalEdge conditionalEdge) {
			if (conditionalEdge.getCondition() instanceof IColouredNetProcessingCondition condition) {
				T token = stateContainer.getCurrentState().getToken(tokenId);
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
			stateContainer.changeState(ColouredNetOperations.copyToken(targetNodeId, tokenId, tokenFactory));
		} else {
			if (shouldTrigger) {
				stateContainer.changeState(ColouredNetOperations.moveToken(sourceNodeId, targetNodeId, tokenId));
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

}
