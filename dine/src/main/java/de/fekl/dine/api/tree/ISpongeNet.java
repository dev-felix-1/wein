package de.fekl.dine.api.tree;

import java.util.Set;

import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.graph.IDirectedGraph;

/**
 * Directed acyclic graph with one root node and n leafs
 * 
 * @author felix
 *
 */
public interface ISpongeNet<N extends INode> extends IDirectedGraph<N> {

	INode getRoot();

	boolean isRoot(N node);

	Set<N> getLeafs();

	boolean isLeaf(N node);

}