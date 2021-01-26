package de.fekl.stat.core.api.state;

import java.util.List;

import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;

public interface IHistory<S> {

	S getInitialState();

	List<IStateHasChangedEvent<S,IStateChangeOperation<S>>> getChanges();
}
