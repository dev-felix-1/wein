package de.fekl.stat.core.impl.events;

import java.util.ArrayList;
import java.util.List;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IEventListener;

public class SimpleEventBus<E extends IEvent> implements IEventBus<E> {

	private final List<IEventListener<E>> listeners = new ArrayList<>();

	@Override
	public void post(E event) {
		handleEvent(event);
	}

	@Override
	public void register(IEventListener<E> listener) {
		listeners.add(listener);
	}

	protected void handleEvent(E event) {
		if (event != null) {
			for (IEventListener<E> listener : listeners) {
				listener.handleEvent(event);
			}
		}
	}

}
