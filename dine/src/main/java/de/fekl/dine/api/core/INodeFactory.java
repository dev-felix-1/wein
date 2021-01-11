package de.fekl.dine.api.core;

public interface INodeFactory<N extends INode> {

	N createNode();

	<E extends N> E createNode(Class<E> clazz);

	N createNode(String nodeId);

	<E extends N> E createNode(String nodeId, Class<E> clazz);

}
