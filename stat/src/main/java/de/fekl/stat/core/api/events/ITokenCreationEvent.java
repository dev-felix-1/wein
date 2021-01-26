package de.fekl.stat.core.api.events;

import de.fekl.stat.core.api.state.operations.ITokenCreationOperation;
import de.fekl.stat.core.api.token.IToken;

public interface ITokenCreationEvent<T extends IToken>
		extends ITokenStoreStateChangeEvent<T, ITokenCreationOperation<T>> {

}
