package de.fekl.dine.core.api.graph;

import java.util.Map;

import de.fekl.dine.core.api.node.INode;

/**
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N>
 * @param <G>
 */
public interface IGraphRegistry<N extends INode, G extends IGraph<N>> {

	public String register(G graph);

	public void register(String graphId, G graph);

	public void unRegister(String graphId);

	public G find(String graphId);

	public boolean contains(String graphId);

	public Map<String, G> getMap();

}
