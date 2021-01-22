package de.fekl.stat.core.api.state.net;

import java.util.List;

import de.fekl.stat.core.api.state.IStateChangeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public interface ITokenMergeOperation<T extends IToken> extends IStateChangeOperation<ITokenStore<T>> {
	String getTargetNodeId();

	List<T> mergedTokens();

	T getResultToken();
}
