package de.fekl.stat.core.api.state;

import java.util.LinkedList;
import java.util.List;

import de.fekl.dine.util.Precondition;
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.impl.events.SimpleStateChangedEvent;

public abstract class AbstractStateContainer<S> implements IStateContainer<S> {

	private S currentState;
	private S initialState;
	private IEventBus<IEvent> eventBus;

	private List<IStateHasChangedEvent<S>> stateChangeEvents = new LinkedList<>();

	protected AbstractStateContainer(S initialState, IEventBus<IEvent> eventBus) {
		super();
		Precondition.isNotNull(initialState);
		Precondition.isNotNull(eventBus);
		this.currentState = initialState;
		this.initialState = initialState;
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
		var stateChangedEvent = new SimpleStateChangedEvent<>(operation, sourceState, targetState);
		stateChangeEvents.add(stateChangedEvent);
		eventBus.post(stateChangedEvent);
	}

	@Override
	public void reset() {
		currentState = initialState;
	}

	@Override
	public IHistory<S> getHistory() {
		return new AbstractHistory<S>(initialState, stateChangeEvents) {

		};
	}

}
