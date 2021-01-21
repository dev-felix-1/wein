package de.fekl.dine.core.api.edge;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <E>
 * @param <F>
 * @param <B>
 */
public interface IEdgeBuilder<E extends IEdge, F extends IEdgeFactory<E>, B extends IEdgeBuilder<E, F, B>> {

	E build();

	B source(String nodeId);

	B target(String nodeId);

	B setEdgeFactory(F factory);

	F getEdgeFactory();

}
