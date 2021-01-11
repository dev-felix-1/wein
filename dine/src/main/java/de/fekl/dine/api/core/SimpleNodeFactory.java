package de.fekl.dine.api.core;

import de.fekl.baut.ReflectionUtils;

public class SimpleNodeFactory implements INodeFactory<INode> {

	@Override
	public INode createNode(String nodeId) {
		return new SimpleNode(nodeId);
	}

	@Override
	public INode createNode() {
		return new SimpleNode(NodeNames.generateNodeName());
	}

	@Override
	public <E extends INode> E createNode(String nodeId, Class<E> clazz) {
		return ReflectionUtils.createInstanceUnsafe(clazz, nodeId);
	}

	@Override
	public <E extends INode> E createNode(Class<E> clazz) {
		return ReflectionUtils.createInstanceUnsafe(clazz, NodeNames.generateNodeName());
	}

}
