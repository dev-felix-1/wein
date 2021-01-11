package de.fekl.dine.api.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.baut.Precondition;
import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.INode;

public class SimpleDirectedGraph<N extends INode> implements IDirectedGraph<N> {

	public static volatile boolean ALLOW_EMPTY_NODE_SET = false;

	private final Map<String, N> nodes;
	private final List<IEdge> edges;

	public SimpleDirectedGraph(Set<N> nodes, List<IEdge> edges) {
		if (!ALLOW_EMPTY_NODE_SET) {
			Precondition.isNotEmpty(nodes);
		}
		Precondition.isNotNull(edges);
		Map<String, N> nodeNamesMap = nodes.stream().collect(Collectors.toUnmodifiableMap(n -> n.getId(), n -> n));
		for (IEdge edge : edges) {
			if (!nodeNamesMap.containsKey(edge.getSource())) {
				throw new IllegalArgumentException(String.format("Edge Source %s is not part of the nodes set %s",
						edge.getSource(), nodeNamesMap.keySet().stream().collect(Collectors.joining(", "))));
			}
			if (!nodeNamesMap.containsKey(edge.getTarget())) {
				throw new IllegalArgumentException(String.format("Edge Target %s is not part of the nodes set %s",
						edge.getTarget(), nodeNamesMap.keySet().stream().collect(Collectors.joining(", "))));
			}
		}
		this.nodes = nodeNamesMap;
		this.edges = Collections.unmodifiableList(new ArrayList<>(edges));
	}

	@Override
	public Collection<N> getNodes() {
		return nodes.values();
	}

	@Override
	public List<IEdge> getEdges() {
		return edges;
	}

	@Override
	public boolean contains(String name) {
		return nodes.containsKey(name);
	}

	@Override
	public List<IEdge> getIncomingEdges(String nodeName) {
		Precondition.isNotEmpty(nodeName);
		return edges.stream().filter(e -> e.getTarget().equals(nodeName)).collect(Collectors.toList());
	}

	@Override
	public List<IEdge> getOutgoingEdges(String nodeName) {
		Precondition.isNotEmpty(nodeName);
		return edges.stream().filter(e -> e.getSource().equals(nodeName)).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		//@formatter:off
		var printTemplate = """
		%s {
		    Nodes [%s],
		    Edges [%s]
		}
		""";
		return String.format(printTemplate, this.getClass().getSimpleName(), 
				nodes.keySet().stream().collect(Collectors.joining(", ")),
				edges.stream().map(IEdge::toString).collect(Collectors.joining(", ")));
		//@formatter:on
	}

	@Override
	public boolean contains(N node) {
		return nodes.containsValue(node);
	}

	@Override
	public N getNode(String nodeId) {
		return nodes.get(nodeId);
	}
}