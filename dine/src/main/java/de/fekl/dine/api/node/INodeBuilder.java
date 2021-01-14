package de.fekl.dine.api.node;

import de.fekl.dine.api.base.IBuilder;

public interface INodeBuilder<N extends INode, F extends INodeFactory<N>, B extends INodeBuilder<N, F, B>>
		extends IBuilder<String, N, F, B> {

//	N build();
//
//	B id(String id);

	B setNodeFactory(F factory);

	F getNodeFactory();

}
