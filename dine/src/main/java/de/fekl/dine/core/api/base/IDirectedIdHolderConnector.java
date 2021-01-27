package de.fekl.dine.core.api.base;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <I> the id type
 */
public interface IDirectedIdHolderConnector<I> {

	/**
	 * 
	 * @return the source of the connection, this is where the connection comes from
	 */
	I getSource();

	/**
	 * 
	 * @return the target of the connection, this is where the connection leads to
	 */
	I getTarget();
}
