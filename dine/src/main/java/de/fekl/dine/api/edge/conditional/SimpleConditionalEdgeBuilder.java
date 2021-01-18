package de.fekl.dine.api.edge.conditional;

public class SimpleConditionalEdgeBuilder extends
		AbstractConditionalEdgeBuilder<SimpleConditionalEdge, SimpleConditionalEdgeFactory, SimpleConditionalEdgeBuilder> {

	public SimpleConditionalEdgeBuilder() {
		setEdgeFactory(new SimpleConditionalEdgeFactory());
	}

	@Override
	public SimpleConditionalEdge build() {
		return getEdgeFactory().createEdge(getSourceNodeId(), getTargetNodeId(), getCondition());
	}

}
