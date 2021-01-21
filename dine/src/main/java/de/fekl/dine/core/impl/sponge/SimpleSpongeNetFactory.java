package de.fekl.dine.core.impl.sponge;

import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.api.sponge.ISpongeNetFactory;

public class SimpleSpongeNetFactory<N extends INode> implements ISpongeNetFactory<N> {

	@Override
	public ISpongeNet<N> createSpongeNet(String startNode, IDirectedGraph<N> graph) {
		return new SimpleSpongeNet<N>(graph, startNode);
	}

}
