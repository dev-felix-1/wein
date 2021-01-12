package de.fekl.tran;

@FunctionalInterface
public interface ITransformation<S, T> {

	T transform(S source);

}
