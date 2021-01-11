package de.fekl.esta.api.core;

public interface IContextHasChangedEvent<T> extends IEvent {

	T getContextBefore();
	
	T getContextAfter();

}
