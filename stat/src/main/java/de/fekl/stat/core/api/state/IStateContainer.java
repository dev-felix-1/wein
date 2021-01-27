package de.fekl.stat.core.api.state;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;

public interface IStateContainer<S> {

	S getCurrentState();

	<O extends IStateChangeOperation<S>> void changeState(O operation);

	void reset();
	
	IHistory<S> getHistory();
	
	void setEventBus(IEventBus<IEvent> eventBus);

}
