package de.fekl.stat.core.impl.edge.conditional;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.edge.conditional.ICondition;
import de.fekl.stat.core.api.edge.conditional.ISimpleConditionalEdgeFactory;
import de.fekl.stat.core.api.token.IToken;

public class SimpleConditionalEdgeFactory<N extends INode, T extends IToken>
		implements ISimpleConditionalEdgeFactory<N, T, SimpleConditionalEdge<N, T>> {

	@Override
	public SimpleConditionalEdge<N, T> createEdge(String source, String target, ICondition<N, T> condition) {
		return new SimpleConditionalEdge<>(source, target, condition);
	}

}
