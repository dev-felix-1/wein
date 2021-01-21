package de.fekl.dine.core.api.node;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N>
 */
public interface ISimpleNodeFactory<N extends INode> extends INodeFactory<N> {

	default N createNode() {
		return createNode(null);
	}

	N createNode(String id);

}
