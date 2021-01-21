package de.fekl.stat.core.api.events;

import java.util.concurrent.BlockingQueue;

public interface IEventQueue<E extends IEvent> extends BlockingQueue<E> {

}
