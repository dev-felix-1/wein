package de.fekl.sepe;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.baut.ILogger;
import de.fekl.baut.LogManager;
import de.fekl.baut.RandomNames;
import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.ITokenFactory;
import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.state.TokenNames;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.esta.api.core.IEventQueue;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.esta.api.core.SimpleEventQueue;
import de.fekl.esta.api.core.SimpleStateContainer;

public class ColouredNetProcessingContainer<N extends INode, T extends IToken> {

	private static final ILogger LOG = LogManager.getInstance().getLogger();

	private final IStateContainer<ITokenStore<T>> stateContainer;
	private final ISpongeNet<N> net;

	private final String processingContainerId;

//	private ITokenFactory<T> tokenFactory;
	private boolean running;
	private long stepCounter;

	private final IEventQueue<IEndNodeReachedEvent> endNodesReachedEvents = new SimpleEventQueue<>(32);

//	private final Map<String, IToken> finishedToken = new ConcurrentHashMap<>();

	public ColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		this.net = net;
//		this.tokenFactory = tokenFactory;
		stateContainer = new SimpleStateContainer<>(initialState);
		processingContainerId = RandomNames.getRandomName(ColouredNetProcessingContainer.class.getName(), "processor_",
				15);
		running = false;
		stepCounter = 0;
	}

	public void process(T token) {
		String startNodeId = net.getRoot().getId();
		stateContainer.changeState(ColouredNetOperations.putToken(startNodeId, TokenNames.generateTokenName(), token));
		run();
	}

	public void run() {
		running = true;
		while (running) {
			if (running && stateContainer.getCurrentState().getTokenPositions().entrySet().stream()
					.allMatch(e -> net.isLeaf(e.getValue()))) {
				running = false;
			}
			step();
		}
	}

	private synchronized void step() {
		LOG.debug("Processing container %s, step %s with state: %s", processingContainerId, stepCounter++,
				ITokenStore.print(stateContainer.getCurrentState()));
		Map<String, String> tokenToNodeMapping = stateContainer.getCurrentState().getTokenPositions();

		Set<Entry<String, String>> entrySet = tokenToNodeMapping.entrySet();
		entrySet.forEach(entry -> {

			String nodeId = entry.getValue();
			String tokenId = entry.getKey();

			handleToken(stateContainer, tokenId, nodeId);
		});
	}

	protected boolean hasReachedEndNode(String tokenId) {
		boolean endNodeReached = endNodesReachedEvents.size() > 0;
		if (endNodeReached) {
			Set<String> reachedEndNodes = getReachedEndNodes();
			LOG.debug("End-Nodes reached %s", reachedEndNodes);
		}
		return endNodeReached;
	}

	protected Set<String> getReachedEndNodes() {
		ITokenStore<T> currentState = stateContainer.getCurrentState();
		Set<String> endNodes = net.getLeafs().stream().map(l -> l.getId()).collect(Collectors.toSet());
		Set<String> reachedEndNodes = currentState.getTokenPositions().values().stream().distinct()
				.filter(endNodes::contains).collect(Collectors.toSet());
		return reachedEndNodes;
	}

	protected <C extends IStateContainer<ITokenStore<T>>> void handleToken(C stateContainer, String tokenId,
			String sourceNodeId) {
		LOG.debug("Handle Token %s on %s ...", tokenId, sourceNodeId);
		List<IEdge> outgoingEdges = net.getOutgoingEdges(sourceNodeId);
		if (!outgoingEdges.isEmpty()) {
			IEdge firstEdge = outgoingEdges.get(0);
			String targetNodeId = firstEdge.getTarget();
			stateContainer.changeState(ColouredNetOperations.moveToken(sourceNodeId, targetNodeId, tokenId));
		} else {
			endNodesReachedEvents.add(new EndNodeReachedEvent(sourceNodeId, tokenId));
		}
	}

	protected ISpongeNet<N> getNet() {
		return net;
	}

	protected IEventQueue<IEndNodeReachedEvent> getEndNodesReachedEvents() {
		return endNodesReachedEvents;
	}

	protected IStateContainer<ITokenStore<T>> getStateContainer() {
		return stateContainer;
	}

}
