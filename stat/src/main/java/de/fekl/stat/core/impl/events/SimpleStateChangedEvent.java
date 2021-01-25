package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;

public class SimpleStateChangedEvent<T> implements IStateHasChangedEvent<T> {

	private final IStateChangeOperation<T> sourceOperation;
	private final T sourceState;
	private final T targetState;

	public SimpleStateChangedEvent(IStateChangeOperation<T> sourceOperation, T sourceState, T targetState) {
		super();
		this.sourceOperation = sourceOperation;
		this.sourceState = sourceState;
		this.targetState = targetState;
	}

	@Override
	public IStateChangeOperation<T> getSourceOperation() {
		return sourceOperation;
	}

	@Override
	public T getSourceState() {
		return sourceState;
	}

	@Override
	public T getTargetState() {
		return targetState;
	}

}
