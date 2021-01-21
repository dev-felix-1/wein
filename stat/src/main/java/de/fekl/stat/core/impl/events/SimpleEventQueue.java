package de.fekl.stat.core.impl.events;

import java.util.concurrent.ArrayBlockingQueue;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventQueue;

public class SimpleEventQueue<E extends IEvent> extends ArrayBlockingQueue<E> implements IEventQueue<E> {

	public SimpleEventQueue(int capacity) {
		super(capacity);
	}

}
