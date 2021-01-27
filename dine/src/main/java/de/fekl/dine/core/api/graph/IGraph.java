package de.fekl.dine.core.api.graph;

import java.util.Collection;

import de.fekl.dine.core.api.base.IIdHolder;
import de.fekl.dine.core.api.node.INode;

/**
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifies the kind of nodes the graph holdes
 */
public interface IGraph<N extends INode> extends IIdHolder<String> {

	Collection<N> getNodes();

	Collection<String> getNodeIds();

	boolean contains(String nodeId);

	boolean contains(N node);

	N getNode(String nodeId);
}
