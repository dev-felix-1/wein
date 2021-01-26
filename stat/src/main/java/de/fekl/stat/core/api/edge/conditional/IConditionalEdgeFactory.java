package de.fekl.stat.core.api.edge.conditional;

import de.fekl.dine.core.api.edge.IEdgeFactory;
import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.token.IToken;

public interface IConditionalEdgeFactory<N extends INode, T extends IToken, E extends IConditionalEdge<N, T>>
		extends IEdgeFactory<E> {

}
