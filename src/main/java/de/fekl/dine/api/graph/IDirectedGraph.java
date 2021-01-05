package de.fekl.dine.api.graph;

import java.util.Collection;
import java.util.List;

import de.fekl.dine.api.core.IEdge;

public interface IDirectedGraph {

	Collection<INode> getNodes();

	List<IEdge> getEdges();

	boolean contains(String nodeId);
	
	boolean contains(INode node);
	
	List<IEdge> getIncomingEdges(String nodeId);

	List<IEdge> getOutgoingEdges(String nodeId);
	
	INode getNode(String nodeId);
}
