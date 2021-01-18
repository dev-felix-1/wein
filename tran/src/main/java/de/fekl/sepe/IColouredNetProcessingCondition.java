package de.fekl.sepe;

import de.fekl.dine.api.edge.conditional.ICondition;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.state.IToken;

public interface IColouredNetProcessingCondition<N extends INode, T extends IToken> extends ICondition {

	boolean evaluate(T token, N sourceNode, N targetNode);

}
