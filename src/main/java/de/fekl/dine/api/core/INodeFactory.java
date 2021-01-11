package de.fekl.dine.api.core;

import de.fekl.dine.api.graph.INode;

public interface INodeFactory<N extends INode> {

	N createNode();
	
	N createNode(String nodeId);
}
