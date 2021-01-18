package de.fekl.esta.api.core;

import de.fekl.baut.Precondition;

public abstract class AbstractStateContainer<S> implements IStateContainer<S> {

	private IStateContainerContext context;
	private S currentState;
	private IEventQueue<IStateHasChangedEvent<S>> stateChangedEvents;
	private IEventQueue<IStateHasChangedEvent<S>> receivedEvents;
	private IEventBus<IStateHasChangedEvent<S>> eventBus;
	private Thread eventBusThread;

	@Deprecated
	protected AbstractStateContainer(S initialState, IEventQueue<IStateHasChangedEvent<S>> stateChangedEvents) {
		super();
		Precondition.isNotNull(initialState);
		Precondition.isNotNull(stateChangedEvents);
		this.currentState = initialState;
		this.stateChangedEvents = stateChangedEvents;
	}

	protected AbstractStateContainer(S initialState, IEventBus<IStateHasChangedEvent<S>> eventBus) {
		super();
		Precondition.isNotNull(initialState);
		Precondition.isNotNull(eventBus);
		this.currentState = initialState;
		this.eventBus = eventBus;
		this.receivedEvents = eventBus.subscribe();
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
		if (stateChangedEvents == null) {
			if (eventBusThread == null) {
				eventBusThread = new Thread((Runnable) eventBus);
				eventBusThread.start();
			}
			postStateChangedEvent(operation, sourceState, targetState);
		} else {
			stateChangedEvents.add(new SimpleStateChangedEvent<>(operation, sourceState, targetState));
		}
	}

	protected <O extends IStateChangeOperation<S>> void postStateChangedEvent(O operation,
			S sourceState, S targetState) {
		eventBus.post(new SimpleStateChangedEvent<>(operation, sourceState, targetState));
	}

	@Override
	public IEventQueue<IStateHasChangedEvent<S>> getStateChangedEvents() {
		if (stateChangedEvents == null) {
			return eventBus.getWaitingEvents();
		} else {
			return stateChangedEvents;
		}
	}

	@Override
	public void onStateChangedEvent(IEventListener<IStateHasChangedEvent<S>> listener) {
		eventBus.register(listener);
	}
	
	@Override
	public IEventQueue<IStateHasChangedEvent<S>> receiveChangeEvents() {
		return receivedEvents;
	}
	
	@Override
	public void waitForStateChangeEventsHandled() {
		eventBus.waitForHandlers();
	}

}
