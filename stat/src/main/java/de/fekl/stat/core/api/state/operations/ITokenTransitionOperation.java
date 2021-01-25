package de.fekl.stat.core.api.state.net;

import de.fekl.stat.core.api.state.IStateChangeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public interface ITokenTransitionOperation<T extends IToken> extends IStateChangeOperation<ITokenStore<T>> {

	T getTransitionedToken();

	String getTargetNodeId();
	
	String getSourceNodeId();
}
