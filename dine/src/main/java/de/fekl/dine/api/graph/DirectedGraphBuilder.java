package de.fekl.dine.api.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.IEdgeFactory;
import de.fekl.dine.api.core.SimpleEdgeFactory;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.INodeBuilder;
import de.fekl.dine.api.node.INodeFactory;
import de.fekl.dine.api.node.INodeFactoryParams;

public class DirectedGraphBuilder<N extends INode> {

	private final Set<N> nodes = new HashSet<>();
	private final List<IEdge> edges = new ArrayList<>();

	private IDirectedGraphFactory<N> directedGraphFactory = new SimpleDirectedGraphFactory<N>();
	private IEdgeFactory edgeFactory = new SimpleEdgeFactory();
	private INodeFactory<N, INodeFactoryParams<N>> nodeFactory;

	public DirectedGraphBuilder<N> setGraphFactory(IDirectedGraphFactory<N> factory) {
		directedGraphFactory = factory;
		return this;
	}

	public DirectedGraphBuilder<N> setEdgeFactory(IEdgeFactory factory) {
		edgeFactory = factory;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <P extends INodeFactoryParams<N>, F extends INodeFactory<N, P>> DirectedGraphBuilder<N> setNodeFactory(F factory) {
		nodeFactory = (INodeFactory<N, INodeFactoryParams<N>>) factory;
		return this;
	}

	public DirectedGraphBuilder<N> addNode(INodeFactoryParams<N> params) {
		N node = nodeFactory.createNode(params);
		return addNode(node);
	}

	public DirectedGraphBuilder<N> addNode(N node) {
		nodes.add(node);
		return this;
	}

	public <B extends INodeBuilder<N, B>> DirectedGraphBuilder<N> addNode(B nodeBuilder) {
		nodes.add(nodeBuilder.build());
		return this;
	}

	public DirectedGraphBuilder<N> addEdge(IEdge edge) {
		edges.add(edge);
		return this;
	}

	public DirectedGraphBuilder<N> addEdge(String sourceNode, String targetNode) {
		edges.add(edgeFactory.createEdge(sourceNode, targetNode));
		return this;
	}

	public IDirectedGraph<N> build() {
		return directedGraphFactory.createDirectedGraph(nodes, edges);
	}

}
