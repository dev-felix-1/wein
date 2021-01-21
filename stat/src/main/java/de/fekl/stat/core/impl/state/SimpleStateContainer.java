package de.fekl.stat.core.impl.state;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.state.AbstractStateContainer;
import de.fekl.stat.core.impl.events.SimpleEventBus;

public class SimpleStateContainer<S> extends AbstractStateContainer<S> {

	public SimpleStateContainer(S initialState) {
		this(initialState, 256);
	}

	public SimpleStateContainer(S initialState, int eventQueueSize) {
		super(initialState, new SimpleEventBus<>(eventQueueSize));
	}
	
	public SimpleStateContainer(S initialState, IEventBus<IEvent> eventBus) {
		super(initialState, eventBus);
	}

}
