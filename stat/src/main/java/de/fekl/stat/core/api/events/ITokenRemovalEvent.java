package de.fekl.stat.core.api.events;

import de.fekl.stat.core.api.state.operations.ITokenRemovalOperation;
import de.fekl.stat.core.api.token.IToken;

public interface ITokenRemovalEvent<T extends IToken>
		extends ITokenStoreStateChangeEvent<T, ITokenRemovalOperation<T>> {

}
