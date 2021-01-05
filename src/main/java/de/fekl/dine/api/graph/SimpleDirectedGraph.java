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

public class SimpleDirectedGraph implements IDirectedGraph {

	private final Map<String, INode> nodes;
	private final List<IEdge> edges;

	public SimpleDirectedGraph(Set<INode> nodes, List<IEdge> edges) {
		Precondition.isNotEmpty(nodes);
		Precondition.isNotNull(edges);
		this.nodes = nodes.stream().collect(Collectors.toUnmodifiableMap(n -> n.getId(), n -> n));
		this.edges = Collections.unmodifiableList(new ArrayList<>(edges));
	}

	@Override
	public Collection<INode> getNodes() {
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
				edges.stream().map(IEdge::print).collect(Collectors.joining(", ")));
		//@formatter:on
	}

	@Override
	public boolean contains(INode node) {
		return nodes.containsValue(node);
	}

	@Override
	public INode getNode(String nodeId) {
		return nodes.get(nodeId);
	}
}
