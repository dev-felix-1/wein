package de.fekl.dine.core.api.sponge;

import java.util.List;
import java.util.Set;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.node.INode;

/**
 * 
 * Directed acyclic graph with one root node and n leafs
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 * 
 * 
 *
 */
public interface ISpongeNet<N extends INode> extends IDirectedGraph<N> {

	N getRoot();

	default String getRootId() {
		return getRoot().getId();
	}

	boolean isRoot(N node);

	boolean isRoot(String nodeId);

	Set<N> getLeafs();

	Set<String> getLeafIds();

	boolean isLeaf(N node);

	boolean isLeaf(String nodeId);

	List<List<IEdge>> getPaths(String sourceNodeId, String targetNodeId);

}
