package de.fekl.wein.api.core.builder;

import java.util.ArrayList;
import java.util.List;

public class CompositeNodesBuilder {

	private List<CompositeNodeBuilder> nodeBuilders = new ArrayList<>();

	public List<CompositeNodeBuilder> getNodeBuilders() {
		return nodeBuilders;
	}
	
	public CompositeNodesBuilder node(CompositeNodeBuilder nodeBuilder) {
		nodeBuilders.add(nodeBuilder);
		return this;
	}

}
