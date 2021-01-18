package de.fekl.dine.api.edge.conditional;

import de.fekl.dine.api.edge.IEdge;

public interface IConditionalEdge extends IEdge {
	
	ICondition getCondition();

}
