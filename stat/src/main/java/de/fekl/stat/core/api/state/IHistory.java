package de.fekl.stat.core.api.state;

import java.util.List;

import de.fekl.stat.core.api.events.IStateHasChangedEvent;

public interface IHistory<S> {

	S getInitialState();

	List<IStateHasChangedEvent<S>> getChanges();
}
