package de.fekl.stat.core.impl.edge.conditional;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.impl.edge.SimpleEdge;
import de.fekl.stat.core.api.edge.conditional.ICondition;
import de.fekl.stat.core.api.edge.conditional.IConditionalEdge;
import de.fekl.stat.core.api.token.IToken;

public class SimpleConditionalEdge<N extends INode, T extends IToken> extends SimpleEdge
		implements IConditionalEdge<N, T> {

	private final ICondition<N, T> condition;

	public SimpleConditionalEdge(String source, String target, ICondition<N, T> condition) {
		super(source, target);
		this.condition = condition;
	}

	@Override
	public ICondition<N, T> getCondition() {
		return condition;
	}

}
