package de.fekl.dine.api.tree;

import de.fekl.dine.api.graph.IDirectedGraph;

public class SimpleSpongeNetFactory implements ISpongeNetFactory {

	@Override
	public ISpongeNet createSpongeNet(String startNode, IDirectedGraph graph) {
		return new SimpleSpongeNet(graph, startNode);
	}

}
