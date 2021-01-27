package de.fekl.stat.core.impl.state;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.state.operations.ITokenCreationOperation;
import de.fekl.stat.core.api.state.operations.ITokenMergeOperation;
import de.fekl.stat.core.api.state.operations.ITokenRemovalOperation;
import de.fekl.stat.core.api.state.operations.ITokenTransitionOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.events.TokenCreationEvent;
import de.fekl.stat.core.impl.events.TokenMergeEvent;
import de.fekl.stat.core.impl.events.TokenRemovalEvent;
import de.fekl.stat.core.impl.events.TokenTransitionEvent;

public class TokenStoreStateContainer<T extends IToken> extends SimpleStateContainer<ITokenStore<T>> {

	public TokenStoreStateContainer(ITokenStore<T> initialState, IEventBus<IEvent> eventBus) {
		super(initialState, eventBus);
	}
	
	public TokenStoreStateContainer(ITokenStore<T> initialState) {
		super(initialState);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>> createEventForStateChangeOperation(
			IStateChangeOperation<ITokenStore<T>> operation, ITokenStore<T> sourceState, ITokenStore<T> targetState) {
		if (ITokenTransitionOperation.class.isAssignableFrom(operation.getClass())) {
			return (IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>) (IStateHasChangedEvent<?, ?>) new TokenTransitionEvent<T>(
					(ITokenTransitionOperation<T>) operation, sourceState, targetState);
		} else if (ITokenRemovalOperation.class.isAssignableFrom(operation.getClass())) {
			return (IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>) (IStateHasChangedEvent<?, ?>) new TokenRemovalEvent<T>(
					(ITokenRemovalOperation<T>) operation, (ITokenStore<T>) sourceState, (ITokenStore<T>) targetState);
		} else if (ITokenCreationOperation.class.isAssignableFrom(operation.getClass())) {
			return (IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>) (IStateHasChangedEvent<?, ?>) new TokenCreationEvent<T>(
					(ITokenCreationOperation<T>) operation, (ITokenStore<T>) sourceState, (ITokenStore<T>) targetState);
		} else if (ITokenMergeOperation.class.isAssignableFrom(operation.getClass())) {
			return (IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>) (IStateHasChangedEvent<?, ?>) new TokenMergeEvent<T>(
					(ITokenMergeOperation<T>) operation, (ITokenStore<T>) sourceState, (ITokenStore<T>) targetState);
		}
		return super.createEventForStateChangeOperation(operation, sourceState, targetState);
	}
}
