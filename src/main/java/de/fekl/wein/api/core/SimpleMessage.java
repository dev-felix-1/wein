package de.fekl.wein.api.core;

public class SimpleMessage<T> implements IMessage<T> {

	private final String id;
	private final T value;

	public SimpleMessage(String id, T value) {
		this.value = value;
		this.id = id;
	}

	public SimpleMessage(SimpleMessage<T> msg) {
		this.value = msg.getValue();
		this.id = msg.getId();
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("SimpleMessage{id:%s,value:%s}", id, value);
	}

	@Override
	public String getId() {
		return id;
	}

}
