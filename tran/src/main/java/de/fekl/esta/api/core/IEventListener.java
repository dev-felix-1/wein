package de.fekl.esta.api.core;

public interface IEventListener<E extends IEvent> {

	void handleEvent(E event);

}
