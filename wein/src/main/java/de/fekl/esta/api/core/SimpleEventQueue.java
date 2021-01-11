package de.fekl.esta.api.core;

import java.util.concurrent.ArrayBlockingQueue;

public class SimpleEventQueue<E extends IEvent> extends ArrayBlockingQueue<E> implements IEventQueue<E> {

	public SimpleEventQueue(int capacity) {
		super(capacity);
	}

}
