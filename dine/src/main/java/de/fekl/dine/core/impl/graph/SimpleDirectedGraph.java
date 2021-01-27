package de.fekl.dine.core.impl.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.graph.AbstractGraph;
import de.fekl.dine.core.api.graph.GraphNames;
import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.util.Precondition;

public class SimpleDirectedGraph<N extends INode> extends AbstractGraph<N> implements IDirectedGraph<N> {

	private final List<IEdge> edges;

	// caching
	private final Map<String, List<IEdge>> incomingEdges = new HashMap<>();
	private final Map<String, List<IEdge>> outgoingEdges = new HashMap<>();
	private String prettyString = null;

	public SimpleDirectedGraph(Set<N> nodes, List<IEdge> edges) {
		this(GraphNames.generateName(), nodes, edges);
	}

	public SimpleDirectedGraph(String id, Set<N> nodes, List<IEdge> edges) {
		super(id, nodes);
		Precondition.isNotNull(edges);
		for (IEdge edge : edges) {
			if (!contains(edge.getSource())) {
				throw new IllegalArgumentException(String.format("Edge Source %s is not part of the nodes set %s",
						edge.getSource(), getNodeIds().stream().collect(Collectors.joining(", "))));
			}
			if (!contains(edge.getTarget())) {
				throw new IllegalArgumentException(String.format("Edge Target %s is not part of the nodes set %s",
						edge.getTarget(), getNodeIds().stream().collect(Collectors.joining(", "))));
			}
		}
		this.edges = Collections.unmodifiableList(new ArrayList<>(edges));
	}

	@Override
	public List<IEdge> getEdges() {
		return edges;
	}

	@Override
	public List<IEdge> getIncomingEdges(String nodeName) {
		Precondition.isNotEmpty(nodeName);
		return incomingEdges.computeIfAbsent(nodeName, s -> computeIncomingEdges(edges, nodeName));
	}

	@Override
	public List<IEdge> getOutgoingEdges(String nodeName) {
		Precondition.isNotEmpty(nodeName);
		return outgoingEdges.computeIfAbsent(nodeName, s -> computeOutgoingEdges(edges, nodeName));
	}

	private static List<IEdge> computeOutgoingEdges(List<IEdge> edges, String nodeName) {
		List<IEdge> result = new ArrayList<>();
		for (IEdge edge : edges) {
			if (edge.getSource().equals(nodeName)) {
				result.add(edge);
			}
		}
		return Collections.unmodifiableList(result);
	}

	private static List<IEdge> computeIncomingEdges(List<IEdge> edges, String nodeName) {
		List<IEdge> result = new ArrayList<>();
		for (IEdge edge : edges) {
			if (edge.getTarget().equals(nodeName)) {
				result.add(edge);
			}
		}
		return Collections.unmodifiableList(result);
	}

	@Override
	public String toString() {
		if (prettyString == null) {
			prettyString = toPrettyString();
		}
		return prettyString;
	}

	public String toPrettyString() {
		//@formatter:off
		var printTemplate = """
				%s {
				Nodes [%s],
				Edges [%s]
				}
				""";
		return String.format(printTemplate, this.getClass().getSimpleName(), 
				getNodeIds().stream().collect(Collectors.joining(", ")),
				edges.stream().map(IEdge::toString).collect(Collectors.joining(", ")));
		//@formatter:on
	}
}
