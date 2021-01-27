package de.fekl.dine.core.api.graph;

import java.util.List;

/**
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifices the kind of nodes the graph holdes
 * @param <G> specifices the kind of graphs this graph is composed of
 */

import de.fekl.dine.core.api.node.INode;

public interface ICompositeGraph<G extends IGraph<N>, N extends INode> extends IGraph<N> {

	/**
	 * 
	 * @return a list of the graphs this graph is composed of
	 */
	List<G> getGraphs();

}
