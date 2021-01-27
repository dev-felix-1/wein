package de.fekl.dine.core.impl.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.graph.GraphNames;
import de.fekl.dine.core.api.graph.ICompositeGraph;
import de.fekl.dine.core.api.graph.IGraph;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.util.Precondition;

public class SimpleCompositeGraph<G extends IGraph<N>, N extends INode> implements ICompositeGraph<G, N> {

	private final String id;
	private final List<G> graphs;
	private final List<IEdge> graphConnectors;

	public SimpleCompositeGraph(String id, List<G> graphs, List<IEdge> graphConnectors) {
		Precondition.isNotEmpty(graphs);
		Map<String, List<N>> collect = graphs.stream().flatMap(n -> n.getNodes().stream())
				.collect(Collectors.groupingBy(n -> n.getId()));
		List<List<N>> collect2 = collect.values().stream().filter(l -> l.size() > 1).collect(Collectors.toList());
		if (!collect2.isEmpty()) {
			throw new IllegalArgumentException(String.format("The nodes in the graphs are not unique %s", collect2));
		}
		this.graphs = Collections.unmodifiableList(graphs);
		this.graphConnectors = graphConnectors;
		this.id = id;
	}

	public SimpleCompositeGraph(List<G> graphs, List<IEdge> graphConnectors) {
		this(GraphNames.generateName(), graphs, graphConnectors);

	}

	@Override
	public Collection<N> getNodes() {
		return graphs.stream().flatMap(n -> n.getNodes().stream()).collect(Collectors.toList());
	}

	@Override
	public Collection<String> getNodeIds() {
		return graphs.stream().flatMap(n -> n.getNodeIds().stream()).collect(Collectors.toList());
	}

	@Override
	public boolean contains(String nodeId) {
		return graphs.stream().anyMatch(g -> g.contains(nodeId));
	}

	@Override
	public boolean contains(N node) {
		return graphs.stream().anyMatch(g -> g.contains(node));
	}

	@Override
	public N getNode(String nodeId) {
		return graphs.stream().filter(g -> g.contains(nodeId)).map(g -> g.getNode(nodeId)).findAny().get();
	}

	@Override
	public List<G> getGraphs() {
		return graphs;
	}

	@Override
	public String getId() {
		return id;
	}

}
