package de.fekl.tran;

public class SimpleMessage<T> implements IMessage<T> {

	private final T value;

	public SimpleMessage(T value) {
		this.value = value;
	}

	public SimpleMessage(SimpleMessage<T> msg) {
		this.value = msg.getValue();
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("SimpleMessage{'%s'}", value);
	}

	@Override
	public IContentType<T> getContentType() {
		return null;
	}

}
