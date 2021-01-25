package de.fekl.stat.core.impl.edge.conditional;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.edge.conditional.AbstractConditionalEdgeBuilder;
import de.fekl.stat.core.api.token.IToken;

public class SimpleConditionalEdgeBuilder<N extends INode, T extends IToken> extends
		AbstractConditionalEdgeBuilder<N, T, SimpleConditionalEdge, SimpleConditionalEdgeFactory, SimpleConditionalEdgeBuilder<N, T>> {

	public SimpleConditionalEdgeBuilder() {
		setEdgeFactory(new SimpleConditionalEdgeFactory());
	}

	@Override
	public SimpleConditionalEdge build() {
		return getEdgeFactory().createEdge(getSourceNodeId(), getTargetNodeId(), getCondition());
	}

}
