package de.fekl.stat.core.impl.events;

import de.fekl.dine.core.api.node.INode;
import de.fekl.stat.core.api.events.IEndNodeReachedEvent;
import de.fekl.stat.core.api.token.IToken;

public class EndNodeReachedEvent<N extends INode, T extends IToken> implements IEndNodeReachedEvent<N, T> {

	private final N node;
	private final T token;

	public EndNodeReachedEvent(N endNode, T token) {
		super();
		this.node = endNode;
		this.token = token;
	}

	@Override
	public N getNode() {
		return node;
	}

	@Override
	public T getToken() {
		return token;
	}

}
