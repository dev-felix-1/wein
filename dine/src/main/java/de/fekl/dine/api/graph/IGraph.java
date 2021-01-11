package de.fekl.dine.api.graph;

import java.util.Collection;

import de.fekl.dine.api.core.INode;

public interface IGraph<N extends INode> {

	Collection<N> getNodes();

	boolean contains(String nodeId);

	boolean contains(N node);

	N getNode(String nodeId);
}