package de.fekl.dine.core.api.graph;

import java.util.List;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;

/**
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N>
 */
public interface IDirectedGraph<N extends INode> extends IGraph<N> {

	List<IEdge> getEdges();

	List<IEdge> getIncomingEdges(String nodeId);

	default List<IEdge> getIncomingEdges(N node) {
		return getIncomingEdges(node.getId());
	}

	List<IEdge> getOutgoingEdges(String nodeId);

	default List<IEdge> getOutgoingEdges(N node) {
		return getOutgoingEdges(node.getId());
	}

}
