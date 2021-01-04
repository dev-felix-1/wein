package de.fekl.dine.api.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.fekl.baut.Precondition;
import de.fekl.dine.api.core.IEdge;

public class SimpleDirectedGraph implements IDirectedGraph {

	private final List<String> nodes;
	private final List<IEdge> edges;

	public SimpleDirectedGraph(List<String> nodes, List<IEdge> edges) {
		Precondition.isNotEmpty(nodes);
		Precondition.isNotNull(edges);
		this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
		this.edges = Collections.unmodifiableList(new ArrayList<>(edges));
	}

	@Override
	public List<String> getNodes() {
		return nodes;
	}

	@Override
	public List<IEdge> getEdges() {
		return edges;
	}

	@Override
	public boolean hasNode(String name) {
		return nodes.contains(name);
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
				nodes.stream().collect(Collectors.joining(", ")),
				edges.stream().map(IEdge::print).collect(Collectors.joining(", ")));
		//@formatter:on
	}
}
