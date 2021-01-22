package de.fekl.stat.core.api.events;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.token.IToken;

public interface IEndNodeReachedEvent<N extends INode, T extends IToken> extends IEvent {

	N getNode();

	T getToken();

}
