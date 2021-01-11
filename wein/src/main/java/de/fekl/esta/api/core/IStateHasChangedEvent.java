package de.fekl.esta.api.core;

public interface IStateHasChangedEvent<T> extends IEvent {

	IStateChangeOperation<T> getSourceOperation();

	T getSourceState();
	
	T getTargetState();

}
