package de.fekl.stat.core.api.state;

import de.fekl.stat.core.api.events.IEvent;

public interface IContextHasChangedEvent<T> extends IEvent {

	T getContextBefore();
	
	T getContextAfter();

}
