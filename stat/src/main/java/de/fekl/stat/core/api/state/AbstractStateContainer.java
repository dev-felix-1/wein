package de.fekl.stat.core.api.state;

import java.util.LinkedList;
import java.util.List;

import de.fekl.dine.util.Precondition;
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.impl.events.AbstractStateHasChangedEvent;

public abstract class AbstractStateContainer<S> implements IStateContainer<S> {

	private S currentState;
	private S initialState;
	private IEventBus<IEvent> eventBus;

	private List<IStateHasChangedEvent<S, IStateChangeOperation<S>>> stateChangeEvents = new LinkedList<>();

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

	@SuppressWarnings("unchecked")
	protected <O extends IStateChangeOperation<S>> void postStateChangedEvent(O operation, S sourceState,
			S targetState) {
		IStateHasChangedEvent<S, IStateChangeOperation<S>> stateChangedEvent = createEventForStateChangeOperation(
				operation, sourceState, targetState);
		if (stateChangedEvent == null) {
			stateChangedEvent = (IStateHasChangedEvent<S, IStateChangeOperation<S>>) new AbstractStateHasChangedEvent.DefaultEvent<S, O>(
					operation, sourceState, targetState);
		}
		stateChangeEvents.add(stateChangedEvent);
		eventBus.post((IEvent) stateChangedEvent);
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

	protected IStateHasChangedEvent<S, IStateChangeOperation<S>> createEventForStateChangeOperation(
			IStateChangeOperation<S> operation, S sourceState, S targetState) {
		return null;
	}

}
