package de.fekl.dine.core.api.base;

/**
 * 
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <I> the type of the id the node will have
 * @param <N> specifies the kind of nodes the graph holdes
 */
public interface IRegistry<I, N extends IIdHolder<I>> {

	void register(N node);

	void unRegister(I id);

	N get(I id);

	boolean contains(I id);

}
