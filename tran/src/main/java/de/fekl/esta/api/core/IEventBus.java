package de.fekl.esta.api.core;

public interface IEventBus<E extends IEvent> {

	IEventQueue<E> subscribe();

	void post(E event);

	void register(IEventListener<E> listener);

	@Deprecated
	IEventQueue<E> getWaitingEvents();

	int getNumberOfWaitingEvents();
	
	void waitForHandlers();

}
