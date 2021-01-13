package de.fekl.tran.api.core;

public interface IMessageFactory {

	<T> IMessage<T> createMessage(T value);

	<T> IMessage<T> copyMessage(IMessage<T> msg);

}
