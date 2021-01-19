package de.fekl.dine.api.edge.conditional;

import de.fekl.dine.api.edge.IEdgeBuilder;

public interface IConditionalEdgeBuilder<E extends IConditionalEdge, F extends IConditionalEdgeFactory<E>, B extends IConditionalEdgeBuilder<E, F, B>>
		extends IEdgeBuilder<E, F, B> {

	B condition(ICondition condition);

}
