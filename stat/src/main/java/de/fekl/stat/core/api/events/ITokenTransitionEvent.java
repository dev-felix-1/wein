package de.fekl.stat.core.api.events;

import de.fekl.stat.core.api.state.operations.ITokenTransitionOperation;
import de.fekl.stat.core.api.token.IToken;

public interface ITokenTransitionEvent<T extends IToken>
		extends ITokenStoreStateChangeEvent<T, ITokenTransitionOperation<T>> {

}
