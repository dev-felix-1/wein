package de.fekl.dine.api.graph;

import java.util.List;

import de.fekl.dine.api.core.IEdge;

public interface IDirectedGraph<N extends INode> extends IGraph<N> {

	List<IEdge> getEdges();

	List<IEdge> getIncomingEdges(String nodeId);

	List<IEdge> getOutgoingEdges(String nodeId);

}
