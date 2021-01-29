package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.token.IToken;

public interface ICondition<N extends INode, T extends IToken> {

	boolean evaluate(IConditionEvaluationContext<N, T> evaluationContext);

}
