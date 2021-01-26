package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.ITokenRemovalEvent;
import de.fekl.stat.core.api.state.operations.ITokenRemovalOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public class TokenRemovalEvent<T extends IToken> extends AbstractTokenStoreChangedEvent<T, ITokenRemovalOperation<T>>
		implements ITokenRemovalEvent<T> {

	public TokenRemovalEvent(ITokenRemovalOperation<T> sourceOperation, ITokenStore<T> sourceState,
			ITokenStore<T> targetState) {
		super(sourceOperation, sourceState, targetState);
	}

}
