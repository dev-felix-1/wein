package de.fekl.dine.api.graph;

import java.util.ArrayList;
import java.util.List;

import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.IEdgeFactory;
import de.fekl.dine.api.core.SimpleEdgeFactory;

public class DirectedGraphBuilder {

	private final List<String> nodes = new ArrayList<>();
	private final List<IEdge> edges = new ArrayList<>();

	private IDirectedGraphFactory directedGraphFactory = new SimpleDirectedGraphFactory();
	private IEdgeFactory edgeFactory = new SimpleEdgeFactory();

	public DirectedGraphBuilder setGraphFactory(IDirectedGraphFactory factory) {
		directedGraphFactory = factory;
		return this;
	}

	public DirectedGraphBuilder setEdgeFactory(IEdgeFactory factory) {
		edgeFactory = factory;
		return this;
	}

	public DirectedGraphBuilder addNode(String name) {
		nodes.add(name);
		return this;
	}

	public DirectedGraphBuilder addEdge(IEdge edge) {
		edges.add(edge);
		return this;
	}

	public DirectedGraphBuilder addEdge(String sourceNode, String targetNode) {
		edges.add(edgeFactory.createEdge(sourceNode, targetNode));
		return this;
	}

	public IDirectedGraph build() {
		return directedGraphFactory.createDirectedGraph(nodes, edges);
	}

}
