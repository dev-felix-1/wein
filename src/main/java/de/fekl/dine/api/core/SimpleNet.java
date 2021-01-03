package de.fekl.dine.api.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fekl.baut.MapFormat;
import de.fekl.baut.Precondition;

public class SimpleNet implements INet {

	private final String id;
	private final Map<String, INode> intermediateNodes = new HashMap<>();
	private final Map<String, INode> endNodes = new HashMap<>();
	private final List<IEdge> edges = new ArrayList<>();

	private INode startNode;
	private String startNodeId;

	public SimpleNet(String id) {
		this.id = id;
	}

	@Override
	public synchronized void addNode(String id, String role, INode node) {
		Precondition.isNotNull(id);
		Precondition.isNotNull(role);
		Precondition.isNotNull(node);
		switch (role) {
		case NodeRoles.START:
			if (startNodeId != null) {
				throw new IllegalStateException(String.format("Startnode %s is already defined!", startNodeId));
			}
			startNodeId = id;
			startNode = node;
			break;
		case NodeRoles.END:
			if (endNodes.containsKey(id)) {
				throw new IllegalStateException(String.format("Endnode %s is already defined!", id));
			}
			endNodes.put(id, node);
			break;
		default:
			if (intermediateNodes.containsKey(id)) {
				throw new IllegalStateException(String.format("Node %s is already defined!", id));
			}
			intermediateNodes.put(id, node);
		}
	}

	@Override
	public void addNode(String id, String role) {
		addNode(id, role, SimpleNode.PLACEHOLDER);
	}

	@Override
	public void addNode(String id) {
		addNode(id, NodeRoles.INTERMEDIATE, SimpleNode.PLACEHOLDER);
	}

	@Override
	public synchronized void addEdge(String srcNodeId, String targetNodeId) {
		preconditionContainsNode(srcNodeId);
		preconditionContainsNode(targetNodeId);
		if (targetNodeId.equals(startNodeId)) {
			throw new IllegalArgumentException(
					String.format("You cannot define an incoming connection to the startNode %s", targetNodeId));
		}
		if (endNodes.containsKey(srcNodeId)) {
			throw new IllegalArgumentException(
					String.format("You cannot define an outcoming connection on an endNode %s", srcNodeId));
		}
		SimpleEdge edge = new SimpleEdge(srcNodeId, targetNodeId);
		if (edges.contains(edge)) {
			throw new IllegalStateException(String.format("Edge %s is already definied", edge));
		}
		edges.add(edge);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Map<String, INode> getAllNodes() {
		Map<String, INode> allNodes = new HashMap<>();
		allNodes.put(startNodeId, startNode);
		allNodes.putAll(intermediateNodes);
		allNodes.putAll(endNodes);
		return allNodes;
	}

	private static String join(Stream<String> stream) {
		return stream.collect(Collectors.joining(", "));
	}

	private static String formatNodeMapEntry(Entry<String, INode> entry) {
		return String.format("{%s (%s)}", entry.getKey(), entry.getValue().print());
	}

	private static <S, T> Stream<Entry<S, T>> stream(Map<S, T> map) {
		return map.entrySet().stream();
	}

	private static Stream<String> transformEntriesToStrings(Map<String, INode> map) {
		return stream(map).map(SimpleNet::formatNodeMapEntry);
	}

	@Override
	public String print() {
		//@formatter:off
		var printTemplate = """
		### SIMPLE NET - %s ### {
		    StartNode: {%s (%s)}
		    IntermediateNodes: [%s]
		    EndNodes: [%s]
		    Connections: [%s]
		}
		""";
		return String.format(printTemplate, 
				id, 
				startNodeId, startNode.print(), 
				join(transformEntriesToStrings(intermediateNodes)),
				join(transformEntriesToStrings(endNodes)),
				join(edges.stream().map(IEdge::print)));
		//@formatter:on
	}

	@Override
	public INode getNode(String id) {
		Precondition.isNotNull(id);
		INode result = null;
		if (id.equals(startNodeId)) {
			result = startNode;
		} else {
			result = endNodes.get(id);
			if (result == null) {
				result = intermediateNodes.get(id);
			}
		}
		return result;
	}

	@Override
	public boolean containsNode(String id) {
		return getNode(id) != null;
	}

	private void preconditionContainsNode(String id) {
		if (!containsNode(id)) {
			throw new IllegalArgumentException(String.format("The node with id %s does not exist", id));
		}
	}

	@Override
	public List<IEdge> getAllEdges() {
		return Collections.unmodifiableList(edges);
	}

	@Override
	public INode getStartNode() {
		return startNode;
	}

	@Override
	public String getStartNodeId() {
		return startNodeId;
	}

	@Override
	public Map<String, INode> getNodesByRole(String role) {
		Precondition.isNotNull(role);
		switch (role) {
		case NodeRoles.START:
			return Collections.singletonMap(startNodeId, startNode);
		case NodeRoles.END:
			return endNodes;
		case NodeRoles.INTERMEDIATE:
			return intermediateNodes;
		default:
			throw new IllegalArgumentException(String.format("Role %s is not supported!", role));
		}
	}

	@Override
	public List<IEdge> getOutgoingEdges(String srcNodeId) {
		return edges.stream().filter(e -> e.getSource().equals(srcNodeId)).collect(Collectors.toList());
	}

	@Override
	public List<IEdge> getIncomingEdges(String targetNodeId) {
		return edges.stream().filter(e -> e.getTarget().equals(targetNodeId)).collect(Collectors.toList());
	}

	@Override
	public void setNode(String id, String role, INode node) {
		preconditionContainsNode(id);
		Precondition.isNotNull(role);
		Precondition.isNotNull(node);
		switch (role) {
		case NodeRoles.START:
			if (startNodeId != null) {
				throw new IllegalStateException(String.format("Startnode %s is already defined!", startNodeId));
			}
			startNodeId = id;
			startNode = node;
			break;
		case NodeRoles.END:
			endNodes.put(id, node);
			break;
		default:
			intermediateNodes.put(id, node);
		}
	}

	@Override
	public void setStartNode(INode node) {
		Precondition.isNotNull(node);
		startNode = node;
	}

}
