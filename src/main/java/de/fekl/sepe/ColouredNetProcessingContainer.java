package de.fekl.sepe;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.baut.ILogger;
import de.fekl.baut.LogManager;
import de.fekl.baut.RandomNames;
import de.fekl.cone.api.core.IColouredNet;
import de.fekl.cone.api.core.IToken;
import de.fekl.cone.api.core.TokenNames;
import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.core.NodeRoles;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.esta.api.core.SimpleStateContainer;

public class ColouredNetProcessingContainer {

	private static final ILogger LOG = LogManager.getInstance().getLogger();

	private final IStateContainer<IColouredNet> stateContainer;
	private final String processingContainerId;

	private boolean running;
	private long stepCounter;

	public ColouredNetProcessingContainer(IColouredNet initialState) {
		stateContainer = new SimpleStateContainer<>(initialState);
		processingContainerId = RandomNames.getRandomName(ColouredNetProcessingContainer.class.getName(), "processor_", 15);
		running = false;
		stepCounter = 0;
	}

	public void process(IToken token) {
		String startNodeId = stateContainer.getCurrentState().getNet().getStartNodeId();
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
		LOG.debug("Processing container %s, step %s", processingContainerId, stepCounter++);
		Map<String, String> tokenToNodeMapping = stateContainer.getCurrentState().getTokenToNodeMapping();
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
		IColouredNet currentState = stateContainer.getCurrentState();
		Map<String, INode> endNodes = currentState.getNet().getNodesByRole(NodeRoles.END);
		Set<String> reachedEndNodes = currentState.getTokenToNodeMapping().values().stream().distinct()
				.filter(endNodes::containsKey).collect(Collectors.toSet());
		boolean endNodeReadched = !reachedEndNodes.isEmpty();
		if (endNodeReadched) {
			LOG.debug("End-Nodes reached %s", reachedEndNodes);
		}
		return endNodeReadched;
	}

	protected void handleToken(IStateContainer<IColouredNet> stateContainer, String tokenId, String sourceNodeId) {
		LOG.debug("Handle Token %s on %s ...", tokenId, sourceNodeId);
		List<IEdge> outgoingEdges = stateContainer.getCurrentState().getNet().getOutgoingEdges(sourceNodeId);
		if (!outgoingEdges.isEmpty()) {
			IEdge firstEdge = outgoingEdges.get(0);
			String targetNodeId = firstEdge.getTarget();
			stateContainer.changeState(ColouredNetOperations.moveToken(sourceNodeId, targetNodeId, tokenId));
		}
	}

}
