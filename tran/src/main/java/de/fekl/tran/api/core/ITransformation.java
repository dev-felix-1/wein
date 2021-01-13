package de.fekl.tran.api.core;

@FunctionalInterface
public interface ITransformation<S, T> {

	T transform(S source);

}
