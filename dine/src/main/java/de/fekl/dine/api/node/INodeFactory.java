package de.fekl.dine.api.node;

public interface INodeFactory<N extends INode, P extends INodeFactoryParams<N>> {

	N createNode(P params);

}
