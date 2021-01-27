package de.fekl.dine.core.api.node;

import de.fekl.dine.core.api.base.IFactory;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifies the kind of nodes the graph holdes
 */
public interface INodeFactory<N extends INode> extends IFactory<String, N> {

}
