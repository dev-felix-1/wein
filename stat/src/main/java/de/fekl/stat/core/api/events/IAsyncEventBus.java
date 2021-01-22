package de.fekl.stat.core.api.events;

public interface IAsyncEventBus<E extends IEvent> extends IEventBus<E> {

	void waitForHandlers();
}
