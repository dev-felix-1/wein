package de.fekl.dine.core.impl.graph;

import java.util.List;
import java.util.Set;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.graph.IDirectedGraphFactory;
import de.fekl.dine.core.api.node.INode;

public class SimpleDirectedGraphFactory<N extends INode> implements IDirectedGraphFactory<N> {

	@Override
	public IDirectedGraph<N> createDirectedGraph(Set<N> nodes, List<IEdge> edges) {
		return new SimpleDirectedGraph<N>(nodes, edges);
	}

}
