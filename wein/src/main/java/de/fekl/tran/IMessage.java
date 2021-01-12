package de.fekl.tran;

public interface IMessage<T> {

	T getValue();

	IContentType<T> getContentType();

}
