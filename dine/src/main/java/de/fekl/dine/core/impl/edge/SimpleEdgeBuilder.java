package de.fekl.dine.core.impl.edge;

import de.fekl.dine.core.api.edge.AbstractEdgeBuilder;

public class SimpleEdgeBuilder extends AbstractEdgeBuilder<SimpleEdge, SimpleEdgeFactory, SimpleEdgeBuilder> {

	public SimpleEdgeBuilder() {
		setEdgeFactory(new SimpleEdgeFactory());
	}

	@Override
	public SimpleEdge build() {
		return getEdgeFactory().createEdge(getSourceNodeId(), getTargetNodeId());
	}

}
