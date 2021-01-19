package de.fekl.esta.api.core;

public interface IEventBus<E extends IEvent> {

	void post(E event);

	void register(IEventListener<E> listener);
	
	void waitForHandlers();

}
