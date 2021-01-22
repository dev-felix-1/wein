package de.fekl.stat.core.impl.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import de.fekl.stat.core.api.events.IAsyncEventBus;
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IEventQueue;

public class SimpleAsyncEventBus<E extends IEvent> implements IAsyncEventBus<E>, Runnable {

	private boolean running;
	private boolean abort;

	private final IEventQueue<E> internalQueue;

	public CountDownLatch waitForHandlersLatch;

	public SimpleAsyncEventBus(int capacity) {
		this.running = false;
		this.abort = false;
		this.internalQueue = new SimpleEventQueue<>(capacity);
	}

	private final List<IEventListener<E>> listeners = new ArrayList<>();

	ReentrantLock lock = new ReentrantLock();
	Condition handlersFinish = lock.newCondition();

	@Override
	public void post(E event) {
		if (running) {
			internalQueue.add(event);
		} else {
			handleEvent(event);
		}
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
			while (running && !abort) {
				try {
					E event = internalQueue.poll(1, TimeUnit.SECONDS);
					if (!abort) {
						waitForHandlersLatch = new CountDownLatch(1);
						System.err.println("start handle...");
						handleEvent(event);
						System.err.println("end handle...");
						waitForHandlersLatch.countDown();
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} else {
			throw new IllegalStateException("Eventbus is already running.");
		}
		System.err.println("Daemon exit...");
		running = false;
	}

	protected void handleEvent(E event) {
		if (event != null) {
			for (IEventListener<E> listener : listeners) {
				listener.handleEvent(event);
			}
		}
	}

	@Override
	public synchronized void waitForHandlers() {
		if (running) {
			try {
				if (waitForHandlersLatch != null) {
					System.err.println("waiting for handler...");
					waitForHandlersLatch.await();
					System.err.println("handlers done.");
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
