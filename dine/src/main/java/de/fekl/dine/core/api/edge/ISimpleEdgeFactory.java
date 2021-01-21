package de.fekl.dine.core.api.edge;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <E>
 */
public interface ISimpleEdgeFactory<E extends IEdge> extends IEdgeFactory<E> {

	E createEdge(String source, String target);

}
