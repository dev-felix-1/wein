package de.fekl.dine.api.core;

public class SimpleNodeFactory implements INodeFactory<SimpleNode> {

	@Override
	public SimpleNode createNode(String nodeId) {
		return new SimpleNode(nodeId);
	}

	@Override
	public SimpleNode createNode() {
		return new SimpleNode(NodeNames.generateNodeName());
	}

}
