package de.fekl.dine.core.api.sponge;

import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.node.INode;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N>
 */
public interface ISpongeNetFactory<N extends INode> {

	ISpongeNet<N> createSpongeNet(String startNode, IDirectedGraph<N> graph);

}
