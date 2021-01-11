package de.fekl.dine.api.tree;

import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.graph.IDirectedGraph;

public class SimpleSpongeNetFactory<N extends INode> implements ISpongeNetFactory<N> {

	@Override
	public ISpongeNet<N> createSpongeNet(String startNode, IDirectedGraph<N> graph) {
		return new SimpleSpongeNet<N>(graph, startNode);
	}

}
