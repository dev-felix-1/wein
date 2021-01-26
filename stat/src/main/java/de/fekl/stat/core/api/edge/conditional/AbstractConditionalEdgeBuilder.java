package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.edge.AbstractEdgeBuilder;
import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.token.IToken;

public abstract class AbstractConditionalEdgeBuilder<
//@formatter:off
	N extends INode,
	T extends IToken,
	E extends IConditionalEdge<N ,T>, 
	F extends IConditionalEdgeFactory<N, T, E>, 
	B extends AbstractConditionalEdgeBuilder<N, T, E, F, B>
>
//@formatter:on
		extends AbstractEdgeBuilder<E, F, B> implements IConditionalEdgeBuilder<N, T, E, F, B> {

	private ICondition<N, T> condition;

	@SuppressWarnings("unchecked")
	@Override
	public B condition(ICondition<N, T> condition) {
		this.condition = condition;
		return (B) this;
	}

	protected ICondition<N, T> getCondition() {
		return condition;
	}

}
