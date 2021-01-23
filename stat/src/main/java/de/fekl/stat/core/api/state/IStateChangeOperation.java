package de.fekl.stat.core.api.state;

@FunctionalInterface
public interface IStateChangeOperation<T> {

	//FIXME should be called applyTo
	public T apply(T state);

}
