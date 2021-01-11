package de.fekl.dine.api.core;

import de.fekl.baut.ReflectionUtils;

public class SimpleNodeFactory implements INodeFactory<SimpleNode> {

	@Override
	public SimpleNode createNode(String nodeId) {
		return new SimpleNode(nodeId);
	}

	@Override
	public SimpleNode createNode() {
		return new SimpleNode(NodeNames.generateNodeName());
	}

	@Override
	public <E extends SimpleNode> E createNode(String nodeId, Class<E> clazz) {
		return ReflectionUtils.createInstanceUnsafe(clazz, nodeId);
	}

	@Override
	public <E extends SimpleNode> E createNode(Class<E> clazz) {
		return ReflectionUtils.createInstanceUnsafe(clazz, NodeNames.generateNodeName());
	}

}
