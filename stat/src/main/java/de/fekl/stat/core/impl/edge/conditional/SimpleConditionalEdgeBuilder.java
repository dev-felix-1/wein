package de.fekl.stat.core.impl.edge.conditional;

import de.fekl.stat.core.api.edge.conditional.AbstractConditionalEdgeBuilder;

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
