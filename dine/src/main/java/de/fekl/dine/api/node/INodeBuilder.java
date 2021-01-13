package de.fekl.dine.api.node;

public interface INodeBuilder<N extends INode, F extends INodeFactory<N>, B extends INodeBuilder<N, F, B>> {

	N build();

	B id(String id);

	B setNodeFactory(F factory);

	F getNodeFactory();

}
