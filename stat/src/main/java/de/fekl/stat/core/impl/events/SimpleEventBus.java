package de.fekl.stat.core.impl.events;

import java.util.HashMap;
import java.util.Map;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IEventListener;

public class SimpleEventBus<E extends IEvent> implements IEventBus<E> {

	private final Map<Class<? extends IEvent>, IEventListener<? extends E>> listeners = new HashMap<>();

	@Override
	public void post(E event) {
		handleEvent(event);
	}

	@Override
	public void register(IEventListener<E> listener) {
		listeners.put(IEvent.class, listener);
	}

	@Override
	public <T extends E> void register(Class<T> clazz, IEventListener<T> listener) {
		listeners.put(clazz, listener);
	}

	protected void handleEvent(E event) {
		if (event != null) {
			Class<? extends IEvent> eventType = event.getClass();
			for (Class<? extends IEvent> type : listeners.keySet()) {
				if (type.isAssignableFrom(eventType)) {
					@SuppressWarnings("unchecked")
					IEventListener<E> listener = (IEventListener<E>) listeners.get(type);
					listener.handleEvent(event);
				}
			}

		}
	}

}
