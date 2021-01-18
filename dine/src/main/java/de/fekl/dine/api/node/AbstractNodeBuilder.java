package de.fekl.dine.api.node;

import de.fekl.dine.api.base.AbstractBuilder;

public abstract class AbstractNodeBuilder<N extends INode, F extends INodeFactory<N>, A extends AbstractNodeBuilder<N, F, A>>
		extends AbstractBuilder<String, N, F, A> implements INodeBuilder<N, F, A> {

	private F factory;

	@SuppressWarnings("unchecked")
	@Override
	public A setNodeFactory(F factory) {
		this.factory = factory;
		return (A) this;
	}

	@Override
	public F getNodeFactory() {
		return factory;
	}

}
