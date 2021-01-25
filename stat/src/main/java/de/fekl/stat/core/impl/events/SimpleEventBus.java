package de.fekl.stat.core.impl.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IEventListener;

public class SimpleEventBus<E extends IEvent> implements IEventBus<E> {

	private final Map<Class<? extends IEvent>, List<IEventListener<? extends E>>> listeners = new HashMap<>();

	@Override
	public void post(E event) {
		handleEvent(event);
	}

	@Override
	public synchronized void register(IEventListener<E> listener) {
		List<IEventListener<? extends E>> listenersList = listeners.computeIfAbsent(IEvent.class, (e)->new LinkedList<>());
		listenersList.add(listener);
	}

	@Override
	public synchronized <T extends E> void register(Class<T> clazz, IEventListener<T> listener) {
		List<IEventListener<? extends E>> listenersList = listeners.computeIfAbsent(clazz, (e)->new LinkedList<>());
		listenersList.add(listener);
	}

	@SuppressWarnings("unchecked")
	protected void handleEvent(E event) {
		if (event != null) {
			Class<? extends IEvent> eventType = event.getClass();
			for (Class<? extends IEvent> type : listeners.keySet()) {
				if (type.isAssignableFrom(eventType)) {
					for(IEventListener<E> listener : (List<IEventListener<E>>)(List<?>)listeners.get(type)) {
						listener.handleEvent(event);
					}
				}
			}

		}
	}

}
