package de.fekl.stat.core.api.state.net;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.edge.conditional.ICondition;
import de.fekl.stat.core.api.token.IToken;

public interface IColouredNetProcessingCondition<N extends INode, T extends IToken> extends ICondition {

	boolean evaluate(T token, N sourceNode, N targetNode);

}
