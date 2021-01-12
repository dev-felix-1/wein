package de.fekl.esta.api.core;

import java.util.concurrent.BlockingQueue;

public interface IEventQueue<E extends IEvent> extends BlockingQueue<E> {

}
