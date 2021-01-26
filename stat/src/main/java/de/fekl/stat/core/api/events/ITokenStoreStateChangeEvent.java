package de.fekl.stat.core.api.events;

import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public interface ITokenStoreStateChangeEvent<T extends IToken, O extends IStateChangeOperation<ITokenStore<T>>>
		extends IStateHasChangedEvent<ITokenStore<T>, O> {

}
