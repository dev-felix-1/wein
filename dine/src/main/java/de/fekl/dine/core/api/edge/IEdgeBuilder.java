package de.fekl.dine.core.api.edge;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <E> the kind of edge that is build
 * @param <F> the factory that is used create build the edge implementation
 * @param <B> the edgebuilder itself (SELF-PATTERN)
 */
public interface IEdgeBuilder<E extends IEdge, F extends IEdgeFactory<E>, B extends IEdgeBuilder<E, F, B>> {

	E build();

	B source(String nodeId);

	B target(String nodeId);

	B setEdgeFactory(F factory);

	F getEdgeFactory();

}
