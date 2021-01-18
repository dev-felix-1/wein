package de.fekl.sepe;

import java.util.List;

import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.ITokenStore;
import de.fekl.esta.api.core.IStateChangeOperation;

public interface ITokenTransitionOperation<T extends IToken> extends IStateChangeOperation<ITokenStore<T>> {

	List<T> getTransitionedToken();

	String getTargetNodeId();
}
