package de.fekl.dine.api.edge.conditional;

import de.fekl.dine.api.edge.SimpleEdge;

public class SimpleConditionalEdge extends SimpleEdge implements IConditionalEdge {

	private final ICondition condition;

	public SimpleConditionalEdge(String source, String target, ICondition condition) {
		super(source, target);
		this.condition = condition;
	}

	@Override
	public ICondition getCondition() {
		return condition;
	}

}
