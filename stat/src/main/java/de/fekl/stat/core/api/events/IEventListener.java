package de.fekl.stat.core.api.events;

public interface IEventListener<E extends IEvent> {

	void handleEvent(E event);

}
