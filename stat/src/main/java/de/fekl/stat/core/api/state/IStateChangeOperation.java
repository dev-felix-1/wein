package de.fekl.stat.core.api.state;

@FunctionalInterface
public interface IStateChangeOperation<T> {

	public T apply(T state);

}
