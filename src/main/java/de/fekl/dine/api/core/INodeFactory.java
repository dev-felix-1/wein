package de.fekl.dine.api.core;

import de.fekl.dine.api.graph.INode;

public interface INodeFactory {

	INode createNode(String nodeId);
}
