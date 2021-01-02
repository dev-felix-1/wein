package de.fekl.wein.api.core;

@FunctionalInterface
public interface ITransformation<S, T> {

	T transform(S source);

}
