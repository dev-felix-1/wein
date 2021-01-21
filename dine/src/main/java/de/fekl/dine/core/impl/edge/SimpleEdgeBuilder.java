package de.fekl.dine.core.impl.edge;

public class SimpleEdgeBuilder extends AbstractEdgeBuilder<SimpleEdge, SimpleEdgeFactory, SimpleEdgeBuilder> {

	public SimpleEdgeBuilder() {
		setEdgeFactory(new SimpleEdgeFactory());
	}

	@Override
	public SimpleEdge build() {
		return getEdgeFactory().createEdge(getSourceNodeId(), getTargetNodeId());
	}

}
