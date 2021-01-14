package de.fekl.dine.api.base;

public interface IBuilder<I, N extends IIdHolder<I>, F extends IFactory<I, N>, B extends IBuilder<I, N, F, B>> {

	N build();

	B id(I id);

//	B setFactory(F factory);
//
//	F getFactory();

}
