package de.fekl.cone.api.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.fekl.baut.Precondition;
import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.INet;
import de.fekl.dine.api.core.SimpleImmutableNet;

public class SimpleColouredNet implements IColouredNet {

	private final String id;
	private final INet net;

	private final Map<String, String> tokenToNodeMapping = new HashMap<>();
	private final Map<String, IToken> tokens = new HashMap<>();

	public SimpleColouredNet(String id, INet net) {
		super();
		Precondition.isNotEmpty(id);
		Precondition.isNotNull(net);
		this.id = id;
		this.net = new SimpleImmutableNet(net);
	}

	public SimpleColouredNet(IColouredNet net) {
		super();
		Precondition.isNotNull(net);
		this.id = net.getId();
		this.net = net.getNet();
		net.getTokenToNodeMapping().forEach((tokenId,nodeId)->{
			this.putToken(nodeId, tokenId, net.getToken(tokenId));
		});
	}

	private Map<String, List<String>> getBaskets() {
		Map<String, List<String>> nodeToTokenMapping = new HashMap<>();
		tokenToNodeMapping.entrySet().forEach(entry -> {
			List<String> basket = nodeToTokenMapping.computeIfAbsent(entry.getValue(), a -> new ArrayList<>());
			basket.add(entry.getKey());
		});
		return nodeToTokenMapping;
	}

	@Override
	public INet getNet() {
		return net;
	}

	@Override
	public synchronized void putToken(String nodeId, String tokenId, IToken token) {
		preconditionContainsNode(nodeId);
		Precondition.isNotEmpty(tokenId);
		Precondition.isNotNull(token);
		if (tokens.containsKey(tokenId)) {
			throw new IllegalStateException(String.format("TokenId %s is already taken.", tokenId));
		}
		tokens.put(tokenId, token);
		tokenToNodeMapping.put(tokenId, nodeId);
	}

	@Override
	public synchronized void removeToken(String nodeId, String tokenId) {
		preconditionContainsNode(nodeId);
		Precondition.isNotEmpty(tokenId);
		if (tokens.containsKey(tokenId)) {
			tokens.remove(tokenId);
			tokenToNodeMapping.remove(tokenId);
		}

	}

	@Override
	public String print() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("### SIMPLE COLOURED NET - %s ###%n", id));
		sb.append("Nodes: [");
		sb.append(net.getAllNodes().entrySet().stream().map(entry -> String.format("{%s}", entry.getKey()))
				.collect(Collectors.joining(", ")));
		sb.append("]\n");
		sb.append("Connections: [");
		sb.append(net.getAllEdges().stream().map(IEdge::print).collect(Collectors.joining(", ")));
		sb.append("]\n");
		sb.append("Tokens: [");
		sb.append(getBaskets().entrySet().stream()
				.map(entry -> String.format("{%s on %s}", entry.getValue(), entry.getKey()))
				.collect(Collectors.joining(", ")));
		sb.append("]\n");
		return sb.toString();
	}

	@Override
	public String getId() {
		return id;
	}

	private void preconditionContainsNode(String id) {
		if (!getNet().containsNode(id)) {
			throw new IllegalArgumentException(String.format("The node with id %s does not exist", id));
		}
	}

	@Override
	public Map<String, String> getTokenToNodeMapping() {
		return Collections.unmodifiableMap(tokenToNodeMapping);
	}

	@Override
	public Map<String, IToken> getAllToken() {
		return Collections.unmodifiableMap(tokens);
	}

	@Override
	public IToken getToken(String tokenId) {
		return tokens.get(tokenId);
	}

	@Override
	public List<String> getTokensOnNode(String nodeId) {
		Precondition.isNotEmpty(nodeId);
		return tokenToNodeMapping.entrySet().stream().filter(e -> e.getValue().equals(nodeId)).map(Entry::getKey)
				.collect(Collectors.toList());
	}

}
