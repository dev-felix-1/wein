package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.ITokenStoreStateChangeEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public abstract class AbstractTokenStoreChangedEvent<T extends IToken, O extends IStateChangeOperation<ITokenStore<T>>>
		extends AbstractStateHasChangedEvent<ITokenStore<T>, O> implements ITokenStoreStateChangeEvent<T, O> {

	public AbstractTokenStoreChangedEvent(O sourceOperation, ITokenStore<T> sourceState, ITokenStore<T> targetState) {
		super(sourceOperation, sourceState, targetState);
	}

}
