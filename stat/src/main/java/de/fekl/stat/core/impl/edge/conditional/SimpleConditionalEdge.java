package de.fekl.stat.core.impl.edge.conditional;

import de.fekl.dine.core.impl.edge.SimpleEdge;
import de.fekl.stat.core.api.edge.conditional.ICondition;
import de.fekl.stat.core.api.edge.conditional.IConditionalEdge;

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
