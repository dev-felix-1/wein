package de.fekl.esta.api.core;

@FunctionalInterface
public interface IStateChangeOperation<T> {

	public T apply(T state);

}
