package de.fekl.dine.api.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.IEdgeFactory;
import de.fekl.dine.api.core.INodeFactory;
import de.fekl.dine.api.core.SimpleEdgeFactory;
import de.fekl.dine.api.core.SimpleNodeFactory;

public class DirectedGraphBuilder {

	private final Set<INode> nodes = new HashSet<>();
	private final List<IEdge> edges = new ArrayList<>();

	private IDirectedGraphFactory directedGraphFactory = new SimpleDirectedGraphFactory();
	private IEdgeFactory edgeFactory = new SimpleEdgeFactory();
	private INodeFactory nodeFactory = new SimpleNodeFactory();

	public DirectedGraphBuilder setGraphFactory(IDirectedGraphFactory factory) {
		directedGraphFactory = factory;
		return this;
	}

	public DirectedGraphBuilder setEdgeFactory(IEdgeFactory factory) {
		edgeFactory = factory;
		return this;
	}

	public DirectedGraphBuilder setNodeFactory(INodeFactory factory) {
		nodeFactory = factory;
		return this;
	}

	public DirectedGraphBuilder addNode(String name) {
		INode node = nodeFactory.createNode(name);
		return addNode(node);
	}

	public DirectedGraphBuilder addNode(INode node) {
		nodes.add(node);
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
