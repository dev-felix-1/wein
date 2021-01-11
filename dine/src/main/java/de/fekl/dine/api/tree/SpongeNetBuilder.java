package de.fekl.dine.api.tree;

import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.graph.IDirectedGraph;

public class SpongeNetBuilder<N extends INode> {

	private IDirectedGraph<N> graph;
	private String startNode;
	private ISpongeNetFactory<N> spongeNetFactory = new SimpleSpongeNetFactory<>();

	public SpongeNetBuilder<N> setSpongeNetFactory(ISpongeNetFactory<N> factory) {
		spongeNetFactory = factory;
		return this;
	}

	public SpongeNetBuilder<N> setGraph(IDirectedGraph<N> graph) {
		this.graph = graph;
		return this;
	}

	public SpongeNetBuilder<N> setStartNode(String startNode) {
		this.startNode = startNode;
		return this;
	}

	public ISpongeNet<N> build() {
		return spongeNetFactory.createSpongeNet(startNode, graph);
	}

}
