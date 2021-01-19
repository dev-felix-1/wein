package de.fekl.dine.api.tree;

import java.util.List;
import java.util.Set;

import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.graph.IDirectedGraph;
import de.fekl.dine.api.node.INode;

/**
 * Directed acyclic graph with one root node and n leafs
 * 
 * @author felix
 *
 */
public interface ISpongeNet<N extends INode> extends IDirectedGraph<N> {

	N getRoot();

	boolean isRoot(N node);

	Set<N> getLeafs();

	boolean isLeaf(N node);

	boolean isLeaf(String nodeId);

	List<List<IEdge>> getPaths(String sourceNodeId, String targetNodeId);

}
