package de.fekl.dine.api.tree;

import de.fekl.dine.api.graph.IDirectedGraph;

public interface ISpongeNetFactory {

	ISpongeNet createSpongeNet(String startNode, IDirectedGraph graph);

}
