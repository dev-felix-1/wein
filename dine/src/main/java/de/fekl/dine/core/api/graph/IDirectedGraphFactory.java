package de.fekl.dine.core.api.graph;

import java.util.List;
import java.util.Set;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifies the kind of nodes the graph holdes
 */
public interface IDirectedGraphFactory<N extends INode> {

	IDirectedGraph<N> createDirectedGraph(Set<N> nodes, List<IEdge> edges);

}
