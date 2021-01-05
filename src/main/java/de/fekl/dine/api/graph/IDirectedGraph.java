package de.fekl.dine.api.graph;

import java.util.List;
import java.util.Set;

import de.fekl.dine.api.core.IEdge;

public interface IDirectedGraph {

	Set<String> getNodes();

	List<IEdge> getEdges();

	boolean hasNode(String name);
	
	List<IEdge> getIncomingEdges(String nodeName);

	List<IEdge> getOutgoingEdges(String nodeName);
}
