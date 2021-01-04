package de.fekl.dine.api.graph;

import java.util.List;

import de.fekl.dine.api.core.IEdge;

public interface IDirectedGraph {

	List<String> getNodes();

	List<IEdge> getEdges();

	boolean hasNode(String name);

	List<IEdge> getIncomingEdges(String nodeName);

	List<IEdge> getOutgoingEdges(String nodeName);
}
