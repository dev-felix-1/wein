package de.fekl.dine.api.tree;

import de.fekl.dine.api.graph.IDirectedGraph;

public class SpongeNetBuilder {

	private IDirectedGraph graph;
	private String startNode;
	private ISpongeNetFactory spongeNetFactory = new SimpleSpongeNetFactory();

	public SpongeNetBuilder setSpongeNetFactory(ISpongeNetFactory factory) {
		spongeNetFactory = factory;
		return this;
	}

	public SpongeNetBuilder setGraph(IDirectedGraph graph) {
		this.graph = graph;
		return this;
	}

	public SpongeNetBuilder setStartNode(String startNode) {
		this.startNode = startNode;
		return this;
	}

	public ISpongeNet build() {
		return spongeNetFactory.createSpongeNet(startNode, graph);
	}

}
