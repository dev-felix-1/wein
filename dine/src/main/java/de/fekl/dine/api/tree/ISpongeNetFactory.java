package de.fekl.dine.api.tree;

import de.fekl.dine.api.graph.IDirectedGraph;
import de.fekl.dine.api.node.INode;

public interface ISpongeNetFactory<N extends INode> {

	ISpongeNet<N> createSpongeNet(String startNode, IDirectedGraph<N> graph);

}
