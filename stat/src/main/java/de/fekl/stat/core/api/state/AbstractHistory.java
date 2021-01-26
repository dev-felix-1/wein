package de.fekl.stat.core.api.state;

import java.util.List;

import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;

public abstract class AbstractHistory<S> implements IHistory<S> {

	private final S initialState;
	private final List<IStateHasChangedEvent<S,IStateChangeOperation<S>>> changes;

	AbstractHistory(S initialState, List<IStateHasChangedEvent<S,IStateChangeOperation<S>>> changes) {
		this.initialState = initialState;
		this.changes = changes;
	}

	@Override
	public S getInitialState() {
		return initialState;
	}

	@Override
	public List<IStateHasChangedEvent<S,IStateChangeOperation<S>>> getChanges() {
		return changes;
	}

}
