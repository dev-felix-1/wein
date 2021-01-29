package de.fekl.stat.core.impl.edge.conditional;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.edge.conditional.IConditionEvaluationContext;
import de.fekl.stat.core.api.edge.conditional.IConditionalEdge;
import de.fekl.stat.core.api.token.IToken;

public class SimpleConditionEvaluationContext<N extends INode, T extends IToken>
		implements IConditionEvaluationContext<N, T> {

	private final N sourceNode;
	private final N targetNode;
	private final T token;
	private final IConditionalEdge<N, T> edge;

	public SimpleConditionEvaluationContext(N sourceNode, N targetNode, T token, IConditionalEdge<N, T> edge) {
		super();
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.token = token;
		this.edge = edge;
	}

	@Override
	public N getSourceNode() {
		return sourceNode;
	}

	@Override
	public N getTargetNode() {
		return targetNode;
	}

	@Override
	public T getToken() {
		return token;
	}

	@Override
	public IConditionalEdge<N, T> getEdge() {
		return edge;
	}

}
