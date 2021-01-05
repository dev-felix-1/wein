package de.fekl.wein.api.core;

import de.fekl.baut.ICopyFactory;
import de.fekl.baut.Precondition;
import de.fekl.cone.api.core.ITokenDeprecated;

public class SimpleMessage<T> implements IMessage<T> {

	private T value;

	public SimpleMessage(T value) {
		this.value = value;
	}

	public SimpleMessage(SimpleMessage<T> msg) {
		this.value = msg.getValue();
	}

	@Override
	public ICopyFactory<ITokenDeprecated> getCopyFactory() {
		return new ICopyFactory<ITokenDeprecated>() {

			@Override
			public ITokenDeprecated copy(ITokenDeprecated object) {
				Precondition.hasClass(object, SimpleMessage.class);
				return new SimpleMessage<>(object);
			}

			@Override
			public ITokenDeprecated copy() {
				return copy(SimpleMessage.this);
			}

		};
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("SimpleMessage{%s}", value);
	}

}
