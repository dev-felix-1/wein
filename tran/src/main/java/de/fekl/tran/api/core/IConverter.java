package de.fekl.tran.api.core;

public interface IConverter<
//@formatter:off
	S, 
	T, 
	INVERS extends IConverter<T, S, SELF, INVERS>, 
	SELF extends IConverter<S, T, INVERS, SELF>
//@formatter:on
> extends ITransformer<S, T> {

	INVERS invert();

}
