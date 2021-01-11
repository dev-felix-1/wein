package de.fekl.dine.api.tree;

import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.graph.IDirectedGraph;

public interface ISpongeNetFactory<N extends INode> {

	ISpongeNet<N> createSpongeNet(String startNode, IDirectedGraph<N> graph);

}
