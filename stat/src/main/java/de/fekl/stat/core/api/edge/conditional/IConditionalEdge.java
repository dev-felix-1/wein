package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.edge.IEdge;

public interface IConditionalEdge extends IEdge {

	ICondition getCondition();

}
