package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.token.IToken;

public interface ISimpleConditionalEdgeFactory<N extends INode, T extends IToken, E extends IConditionalEdge<N, T>>
		extends IConditionalEdgeFactory<N, T, E> {

	E createEdge(String source, String target, ICondition<N, T> condition);

}
