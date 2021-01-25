package de.fekl.stat.core.api.state.operations;

import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public interface ITokenRemovalOperation<T extends IToken> extends IStateChangeOperation<ITokenStore<T>> {

	T getRemovedToken();

	String getTargetNodeId();

}
