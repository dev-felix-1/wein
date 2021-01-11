package de.fekl.dine.api.graph;

import java.util.Collection;
import java.util.List;

import de.fekl.dine.api.core.IEdge;

public interface IDirectedGraph<N extends INode> {

	Collection<N> getNodes();

	List<IEdge> getEdges();

	boolean contains(String nodeId);

	boolean contains(N node);

	List<IEdge> getIncomingEdges(String nodeId);

	List<IEdge> getOutgoingEdges(String nodeId);

	N getNode(String nodeId);
}
