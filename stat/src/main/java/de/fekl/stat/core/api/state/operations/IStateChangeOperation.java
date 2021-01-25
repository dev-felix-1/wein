package de.fekl.stat.core.api.state.operations;

@FunctionalInterface
public interface IStateChangeOperation<T> {

	//FIXME should be called applyTo
	public T apply(T state);

}
