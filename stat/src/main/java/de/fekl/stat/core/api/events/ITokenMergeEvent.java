package de.fekl.stat.core.api.events;

import de.fekl.stat.core.api.state.operations.ITokenMergeOperation;
import de.fekl.stat.core.api.token.IToken;

public interface ITokenMergeEvent<T extends IToken> extends ITokenStoreStateChangeEvent<T, ITokenMergeOperation<T>> {

}
