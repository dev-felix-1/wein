package de.fekl.tran.api.core;

public interface IMessage<T> {

	T getValue();

	IContentType<T> getContentType();

}
