package de.fekl.dine.core.api.graph;

import java.util.List;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;

/**
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifies the kind of nodes the graph holdes
 */
public interface IDirectedGraph<N extends INode> extends IGraph<N> {

	/**
	 * 
	 * @return all node connections in a directed format
	 */
	List<IEdge> getEdges();

	/**
	 * 
	 * @return all connections from any nodes that lead to the node with id nodeId
	 */
	List<IEdge> getIncomingEdges(String nodeId);

	/**
	 * 
	 * @return all connections from any nodes that lead to the node with id
	 *         node.getId()
	 */
	default List<IEdge> getIncomingEdges(N node) {
		return getIncomingEdges(node.getId());
	}

	/**
	 * 
	 * @return all connections to any nodes that come from the node with id nodeId
	 */
	List<IEdge> getOutgoingEdges(String nodeId);

	/**
	 * 
	 * @return all connections to any nodes that come from the node with id
	 *         node.getId()
	 */
	default List<IEdge> getOutgoingEdges(N node) {
		return getOutgoingEdges(node.getId());
	}

}
