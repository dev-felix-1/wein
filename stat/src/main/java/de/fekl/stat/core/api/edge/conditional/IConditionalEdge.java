package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.token.IToken;

public interface IConditionalEdge<N extends INode, T extends IToken> extends IEdge {

	ICondition<N, T> getCondition();

}
