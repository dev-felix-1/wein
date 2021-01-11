package de.fekl.dine.api.graph;

import java.util.List;
import java.util.Set;

import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.INode;

public interface IDirectedGraphFactory<N extends INode> {

	IDirectedGraph<N> createDirectedGraph(Set<N> nodes, List<IEdge> edges);

}
