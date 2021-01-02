package de.fekl.esta.api.core;

public interface IStateContainer<S> {

	S getCurrentState();

	void changeState(IStateChangeOperation<S> operation);

	IEventQueue<IStateHasChangedEvent<S>> getStateChangedEvents();

}
