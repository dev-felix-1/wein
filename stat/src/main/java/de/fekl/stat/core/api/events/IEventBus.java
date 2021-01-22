package de.fekl.stat.core.api.events;

public interface IEventBus<E extends IEvent> {

	void post(E event);

	void register(IEventListener<E> listener);

}
