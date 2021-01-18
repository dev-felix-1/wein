package de.fekl.esta.api.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleEventBus<E extends IEvent> implements IEventBus<E>, Runnable {

	private boolean running;
	private boolean abort;

	private final IEventQueue<E> internalQueue;
	private final int capacity;

	public CountDownLatch waitForHandlersLatch;

	public SimpleEventBus(int capacity) {
		this.running = false;
		this.abort = false;
		this.capacity = capacity;
		this.internalQueue = new SimpleEventQueue<>(capacity);
	}

	private final List<IEventListener<E>> listeners = new ArrayList<>();
	private final List<IEventQueue<E>> subscriptions = new ArrayList<>();

	ReentrantLock lock = new ReentrantLock();
	Condition handlersFinish = lock.newCondition();

	@Override
	public IEventQueue<E> subscribe() {
		SimpleEventQueue<E> sub = new SimpleEventQueue<>(capacity);
		subscriptions.add(sub);
		return sub;
	}

	@Override
	public void post(E event) {
		internalQueue.add(event);
	}

	@Override
	public void register(IEventListener<E> listener) {
		listeners.add(listener);
	}

	public void abort() {
		abort = true;
	}

	public void run() {
		if (!running) {
			running = true;
			while (running) {
				if (abort) {
					running = false;
				}
				try {
					E event = internalQueue.poll(1, TimeUnit.SECONDS);
					if (event != null) {
						for (IEventQueue<E> sub : subscriptions) {
							sub.add(event);
						}
						waitForHandlersLatch = new CountDownLatch(1);
						for (IEventListener<E> listener : listeners) {
							listener.handleEvent(event);
						}
						waitForHandlersLatch.countDown();
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} else {
			throw new IllegalStateException("Eventbus is already running.");
		}
	}

	@Override
	public int getNumberOfWaitingEvents() {
		return internalQueue.size();
	}

	@Override
	public IEventQueue<E> getWaitingEvents() {
		return internalQueue;
	}

	@Override
	public synchronized void waitForHandlers() {
		try {
			if (waitForHandlersLatch != null) {
				waitForHandlersLatch.await();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}

}
