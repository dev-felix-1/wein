package de.fekl.esta.api.core;

public class SimpleStateContainer<S> extends AbstractStateContainer<S> {

	public SimpleStateContainer(S initialState) {
		this(initialState, 256);
	}

	public SimpleStateContainer(S initialState, int eventQueueSize) {
		super(initialState, new SimpleEventQueue<>(eventQueueSize));
	}

}
