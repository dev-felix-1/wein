package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.ITokenTransitionEvent;
import de.fekl.stat.core.api.state.operations.ITokenTransitionOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public class TokenTransitionEvent<T extends IToken>
		extends AbstractTokenStoreChangedEvent<T, ITokenTransitionOperation<T>> implements ITokenTransitionEvent<T>

{
	public TokenTransitionEvent(ITokenTransitionOperation<T> sourceOperation, ITokenStore<T> sourceState,
			ITokenStore<T> targetState) {
		super(sourceOperation, sourceState, targetState);
	}

}
