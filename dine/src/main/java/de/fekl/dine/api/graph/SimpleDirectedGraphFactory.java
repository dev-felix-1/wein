package de.fekl.dine.api.graph;

import java.util.List;
import java.util.Set;

import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.node.INode;

public class SimpleDirectedGraphFactory<N extends INode> implements IDirectedGraphFactory<N> {

	@Override
	public IDirectedGraph<N> createDirectedGraph(Set<N> nodes, List<IEdge> edges) {
		return new SimpleDirectedGraph<N>(nodes, edges);
	}

}
