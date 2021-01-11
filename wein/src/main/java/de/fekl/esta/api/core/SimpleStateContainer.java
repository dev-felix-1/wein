package de.fekl.esta.api.core;

public class SimpleStateContainer<S> extends AbstractStateContainer<S> {

	public SimpleStateContainer(S initialState) {
		super(initialState, new SimpleEventQueue<>(256));
	}

}
