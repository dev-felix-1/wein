package de.fekl.dine.api.graph;

import java.util.Collection;

import de.fekl.dine.api.node.INode;

public interface IGraph<N extends INode> {

	Collection<N> getNodes();
	
	Collection<String> getNodeIds();

	boolean contains(String nodeId);

	boolean contains(N node);

	N getNode(String nodeId);
}
