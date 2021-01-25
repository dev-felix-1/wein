package de.fekl.stat.core.api.events;

import de.fekl.stat.core.api.state.operations.IStateChangeOperation;

public interface IStateHasChangedEvent<T> extends IEvent {

	IStateChangeOperation<T> getSourceOperation();

	T getSourceState();

	T getTargetState();

}
