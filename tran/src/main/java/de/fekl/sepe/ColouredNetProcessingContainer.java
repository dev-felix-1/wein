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
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.esta.api.core.IStateHasChangedEvent;
import de.fekl.esta.api.core.SimpleEventBus;
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

	private final SimpleEventBus<IEvent> processingEvents;

	public ColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		this.net = net;
		this.tokenFactory = tokenFactory;
		stateContainer = new SimpleStateContainer<>(initialState, getNet().getNodes().size() * 2);
		processingContainerId = RandomNames.getRandomName(ColouredNetProcessingContainer.class.getName(), "processor_",
				15);
		running = false;
		abort = false;
		stepCounter = 0;
		// FIXME find all paths from start to end and set it like this
		processingEvents = new SimpleEventBus<>(64);
	}

	public void process(T token) {
		String startNodeId = net.getRoot().getId();
		new Thread(processingEvents).start();
		stateContainer.changeState(ColouredNetOperations.putToken(startNodeId, TokenNames.generateTokenName(), token));
		run();
	}

	protected void setRunning(boolean running) {
		this.running = running;
	}

	public void run() {
		setRunning(true);
		processingEvents.post(new ProcessStartedEvent());
		processingEvents.waitForHandlers();
		while (running) {
			if (running && stateContainer.getCurrentState().getTokenPositions().entrySet().stream()
					.allMatch(e -> net.isLeaf(e.getValue()))) {
				LOG.debug("Stop running because every token is in a final place.");
				setRunning(false);
			}
			if (abort) {
				setRunning(false);
				processingEvents.post(new ProcessAbortedEvent());
				processingEvents.waitForHandlers();
			} else {
				try {
					IStateHasChangedEvent<ITokenStore<T>> stateChangedEvent = stateContainer.receiveChangeEvents()
							.poll(1, TimeUnit.SECONDS);
					stateContainer.waitForStateChangeEventsHandled();
					if (stateChangedEvent != null) {
						step();
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		processingEvents.post(new ProcessFinishedEvent());
	}

	public void shutdown() {
		processingEvents.waitForHandlers();
		processingEvents.abort();
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
			processingEvents.post(new EndNodeReachedEvent(sourceNodeId, tokenId));
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
		processingEvents.register(listener);
	}

}
