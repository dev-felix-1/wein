package de.fekl.dine.core.api.base;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <I> the id type
 */
public interface IIdHolder<I> {

	/**
	 * 
	 * @return the id, should never be null
	 */
	I getId();
}
