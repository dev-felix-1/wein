package de.fekl.esta.api.core;

import de.fekl.baut.Precondition;

public abstract class AbstractStateContainer<S> implements IStateContainer<S> {
	
	private IStateContainerContext context;
	private S currentState;
	private IEventQueue<IStateHasChangedEvent<S>> stateChangedEvents;

	protected AbstractStateContainer(S initialState, IEventQueue<IStateHasChangedEvent<S>> stateChangedEvents) {
		super();
		Precondition.isNotNull(initialState);
		Precondition.isNotNull(stateChangedEvents);
		this.currentState = initialState;
		this.stateChangedEvents = stateChangedEvents;
	}

	@Override
	public S getCurrentState() {
		return currentState;
	}

	@Override
	public synchronized void changeState(IStateChangeOperation<S> operation) {
		S sourceState = currentState;
		S targetState = operation.apply(currentState);
		currentState = targetState;
		stateChangedEvents.add(new SimpleStateChangedEvent<>(operation, sourceState, targetState));
	}

	@Override
	public IEventQueue<IStateHasChangedEvent<S>> getStateChangedEvents() {
		return stateChangedEvents;
	}

}
