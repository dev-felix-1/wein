package de.fekl.dine.api.node;

public interface INodeBuilder<N extends INode, B extends INodeBuilder<N, B>> {

	N build();
	
	B id(String id);

	<P extends INodeFactoryParams<N>, F extends INodeFactory<N, P>> B setNodeFactory(F factory);

}
