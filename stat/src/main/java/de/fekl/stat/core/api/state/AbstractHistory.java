package de.fekl.stat.core.api.state;

import java.util.List;

import de.fekl.stat.core.api.events.IStateHasChangedEvent;

public abstract class AbstractHistory<S> implements IHistory<S> {

	private final S initialState;
	private final List<IStateHasChangedEvent<S>> changes;

	AbstractHistory(S initialState, List<IStateHasChangedEvent<S>> changes) {
		this.initialState = initialState;
		this.changes = changes;
	}

	@Override
	public S getInitialState() {
		return initialState;
	}

	@Override
	public List<IStateHasChangedEvent<S>> getChanges() {
		return changes;
	}

}
