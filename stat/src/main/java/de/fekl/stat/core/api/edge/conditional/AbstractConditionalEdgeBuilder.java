package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.edge.AbstractEdgeBuilder;

public abstract class AbstractConditionalEdgeBuilder<
//@formatter:off
	E extends IConditionalEdge, 
	F extends IConditionalEdgeFactory<E>, 
	B extends AbstractConditionalEdgeBuilder<E, F, B>
>
//@formatter:on
		extends AbstractEdgeBuilder<E, F, B> implements IConditionalEdgeBuilder<E, F, B> {

	private ICondition condition;

	@SuppressWarnings("unchecked")
	@Override
	public B condition(ICondition condition) {
		this.condition = condition;
		return (B) this;
	}

	protected ICondition getCondition() {
		return condition;
	}

}
