package de.fekl.dine.api.tree;

import java.util.Set;

import de.fekl.dine.api.graph.IDirectedGraph;
import de.fekl.dine.api.graph.INode;

/**
 * Directed acyclic graph with one root node and n leafs
 * 
 * @author felix
 *
 */
public interface ISpongeNet extends IDirectedGraph {

	INode getRoot();

	boolean isRoot(INode node);

	Set<INode> getLeafs();

	boolean isLeaf(INode node);

}
