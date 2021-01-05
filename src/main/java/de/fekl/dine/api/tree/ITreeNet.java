package de.fekl.dine.api.tree;

import java.util.Set;

import de.fekl.dine.api.graph.IDirectedGraph;

/**
 * Directed acyclic graph with one root node and n leafs
 * 
 * @author felix
 *
 */
public interface ITreeNet extends IDirectedGraph {

	String getRoot();

	boolean isRoot(String nodeName);

	Set<String> getLeafs();

	boolean isLeaf(String nodeName);

}
