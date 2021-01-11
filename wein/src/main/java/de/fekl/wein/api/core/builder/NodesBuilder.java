package de.fekl.wein.api.core.builder;

import java.util.ArrayList;
import java.util.List;

public class NodesBuilder {

	private List<NodeBuilder> nodeBuilders = new ArrayList<>();

	public List<NodeBuilder> getNodeBuilders() {
		return nodeBuilders;
	}
	
	public NodesBuilder node(NodeBuilder nodeBuilder) {
		nodeBuilders.add(nodeBuilder);
		return this;
	}

}
