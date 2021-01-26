package de.fekl.stat.core.api.events;

import de.fekl.stat.core.api.state.operations.IStateChangeOperation;

public interface IStateHasChangedEvent<S, O extends IStateChangeOperation<S>> extends IEvent {

	O getSourceOperation();

	S getSourceState();

	S getTargetState();

}
