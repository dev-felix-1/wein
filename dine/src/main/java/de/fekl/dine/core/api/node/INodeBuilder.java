package de.fekl.dine.core.api.node;

import de.fekl.dine.core.api.base.IBuilder;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N>
 * @param <F>
 * @param <B>
 */
public interface INodeBuilder<N extends INode, F extends INodeFactory<N>, B extends INodeBuilder<N, F, B>>
		extends IBuilder<String, N, F, B> {

	B setNodeFactory(F factory);

	F getNodeFactory();

}
