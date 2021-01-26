package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.edge.IEdgeBuilder;
import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.token.IToken;

public interface IConditionalEdgeBuilder<
//@formatter:off
	N extends INode,
	T extends IToken,
	E extends IConditionalEdge<N, T>, 
	F extends IConditionalEdgeFactory<N, T, E>, 
	B extends IConditionalEdgeBuilder<N, T, E, F, B>
>
//@formatter:on
		extends IEdgeBuilder<E, F, B> {

	B condition(ICondition<N, T> condition);

}
