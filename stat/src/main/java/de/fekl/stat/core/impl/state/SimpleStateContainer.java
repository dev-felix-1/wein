package de.fekl.stat.core.impl.state;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.state.AbstractStateContainer;

public class SimpleStateContainer<S> extends AbstractStateContainer<S> {

	public SimpleStateContainer(S initialState, IEventBus<IEvent> eventBus) {
		super(initialState, eventBus);
	}


}
