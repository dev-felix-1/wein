package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.token.IToken;

public interface IConditionEvaluationContext<N extends INode, T extends IToken> {

	N getSourceNode();

	N getTargetNode();

	T getToken();

	IConditionalEdge<N, T> getEdge();

}
