package de.fekl.esta.api.core;

public interface IStateContainer<S> {

	S getCurrentState();

	<O extends IStateChangeOperation<S>> void changeState(O operation);

}
