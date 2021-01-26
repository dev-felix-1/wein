package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.ITokenCreationEvent;
import de.fekl.stat.core.api.state.operations.ITokenCreationOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public class TokenCreationEvent<T extends IToken> extends AbstractTokenStoreChangedEvent<T, ITokenCreationOperation<T>>
		implements ITokenCreationEvent<T> {

	public TokenCreationEvent(ITokenCreationOperation<T> sourceOperation, ITokenStore<T> sourceState,
			ITokenStore<T> targetState) {
		super(sourceOperation, sourceState, targetState);
	}

}
