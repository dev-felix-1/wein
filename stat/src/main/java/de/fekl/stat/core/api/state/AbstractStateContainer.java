package de.fekl.stat.core.api.state;

import de.fekl.dine.util.Precondition;
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.impl.events.SimpleStateChangedEvent;

public abstract class AbstractStateContainer<S> implements IStateContainer<S> {

	private IStateContainerContext context;
	private S currentState;
	private IEventBus<IEvent> eventBus;

	protected AbstractStateContainer(S initialState, IEventBus<IEvent> eventBus) {
		super();
		Precondition.isNotNull(initialState);
		Precondition.isNotNull(eventBus);
		this.currentState = initialState;
		this.eventBus = eventBus;
	}

	@Override
	public S getCurrentState() {
		return currentState;
	}

	@Override
	public synchronized <O extends IStateChangeOperation<S>> void changeState(O operation) {
		S sourceState = currentState;
		S targetState = operation.apply(currentState);
		currentState = targetState;
		postStateChangedEvent(operation, sourceState, targetState);
	}

	protected <O extends IStateChangeOperation<S>> void postStateChangedEvent(O operation, S sourceState,
			S targetState) {
		eventBus.post(new SimpleStateChangedEvent<>(operation, sourceState, targetState));
	}

}
