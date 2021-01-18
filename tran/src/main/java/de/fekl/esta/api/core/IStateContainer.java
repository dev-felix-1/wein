package de.fekl.esta.api.core;

public interface IStateContainer<S> {

	S getCurrentState();

	<O extends IStateChangeOperation<S>> void changeState(O operation);

	@Deprecated
	IEventQueue<IStateHasChangedEvent<S>> getStateChangedEvents();

	void onStateChangedEvent(IEventListener<IStateHasChangedEvent<S>> listener);

	IEventQueue<IStateHasChangedEvent<S>> receiveChangeEvents();
	
	void waitForStateChangeEventsHandled();

}
