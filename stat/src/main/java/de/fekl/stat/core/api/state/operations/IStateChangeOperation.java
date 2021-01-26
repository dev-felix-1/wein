package de.fekl.stat.core.api.state.operations;

@FunctionalInterface
public interface IStateChangeOperation<S> {

	//FIXME should be called applyTo
	public S apply(S state);

}
