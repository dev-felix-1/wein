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
import de.fekl.dine.api.core.NodeRoles;
import de.fekl.dine.api.graph.INode;
import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.ITokenFactory;
import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.state.SimpleTokenFactory;
import de.fekl.dine.api.state.TokenNames;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.esta.api.core.SimpleStateContainer;

public class ColouredNetProcessingContainer {

	private static final ILogger LOG = LogManager.getInstance().getLogger();

	private final IStateContainer<ITokenStore> stateContainer;
	private final ISpongeNet net;

	private final String processingContainerId;

	private ITokenFactory tokenFactory = new SimpleTokenFactory();
	private boolean running;
	private long stepCounter;

	public ColouredNetProcessingContainer(ISpongeNet net, ITokenStore initialState) {
		this.net = net;
		stateContainer = new SimpleStateContainer<>(initialState);
		processingContainerId = RandomNames.getRandomName(ColouredNetProcessingContainer.class.getName(), "processor_",
				15);
		running = false;
		stepCounter = 0;
	}

	public void process(IToken token) {
		String startNodeId = net.getRoot().getId();
		stateContainer.changeState(ColouredNetOperations.putToken(startNodeId, TokenNames.generateTokenName(), token));
		run();
	}

	public void run() {
		running = true;
		while (running) {
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
			if (running && isEndNodeReached()) {
				running = false;
			}

		});
	}

	protected boolean isEndNodeReached() {
		ITokenStore currentState = stateContainer.getCurrentState();
		Set<String> endNodes = net.getLeafs().stream().map(l -> l.getId()).collect(Collectors.toSet());
		Set<String> reachedEndNodes = currentState.getTokenPositions().values().stream().distinct()
				.filter(endNodes::contains).collect(Collectors.toSet());
		boolean endNodeReadched = !reachedEndNodes.isEmpty();
		if (endNodeReadched) {
			LOG.debug("End-Nodes reached %s", reachedEndNodes);
		}
		return endNodeReadched;
	}

	protected void handleToken(IStateContainer<ITokenStore> stateContainer, String tokenId, String sourceNodeId) {
		LOG.debug("Handle Token %s on %s ...", tokenId, sourceNodeId);
		List<IEdge> outgoingEdges = net.getOutgoingEdges(sourceNodeId);
		if (!outgoingEdges.isEmpty()) {
			IEdge firstEdge = outgoingEdges.get(0);
			String targetNodeId = firstEdge.getTarget();
			stateContainer.changeState(ColouredNetOperations.moveToken(sourceNodeId, targetNodeId, tokenId));
		}
	}

	protected ISpongeNet getNet() {
		return net;
	}

}
