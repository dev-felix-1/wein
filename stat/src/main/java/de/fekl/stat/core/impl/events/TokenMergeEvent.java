package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.ITokenMergeEvent;
import de.fekl.stat.core.api.state.operations.ITokenMergeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public class TokenMergeEvent<T extends IToken> extends AbstractTokenStoreChangedEvent<T, ITokenMergeOperation<T>>
		implements ITokenMergeEvent<T>

{

	public TokenMergeEvent(ITokenMergeOperation<T> sourceOperation, ITokenStore<T> sourceState,
			ITokenStore<T> targetState) {
		super(sourceOperation, sourceState, targetState);
	}

}
