package de.fekl.dine.core.api.node;

import de.fekl.dine.util.AbstractBuilder;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifies the kind of nodes the graph holdes
 * @param <F>
 * @param <A>
 */
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
