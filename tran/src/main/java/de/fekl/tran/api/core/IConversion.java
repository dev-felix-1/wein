package de.fekl.tran.api.core;

public interface IConversion<S, T> extends ITransformation<S, T> {

	ITransformation<T, S> invert();

}
