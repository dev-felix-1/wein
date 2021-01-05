package de.fekl.dine.api.core;

import de.fekl.dine.api.graph.INode;

public class SimpleNodeFactory implements INodeFactory {

	@Override
	public INode createNode(String nodeId) {
		return new SimpleNode(nodeId);
	}

}
