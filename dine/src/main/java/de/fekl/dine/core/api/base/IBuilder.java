package de.fekl.dine.core.api.base;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <I> The type of Identifier the object to be build uses
 * @param <N> specifies the kind of nodes the graph holdes The type of object to be build
 * @param <F> The factory that is supposed to create the object
 * @param <B> The builder itself (SELF-PATTERN)
 */
public interface IBuilder<I, N extends IIdHolder<I>, F extends IFactory<I, N>, B extends IBuilder<I, N, F, B>> {

	/**
	 * @since 1.0.0
	 * @return The object created from the factory F on basis of the supplied
	 *         building parameters.
	 */
	N build();

	/**
	 * @since 1.0.0
	 * @param id building parameter id
	 * @return the builder (FLUENT-PATTERN)
	 */
	B id(I id);

}
