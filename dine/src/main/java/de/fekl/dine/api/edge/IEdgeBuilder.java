package de.fekl.dine.api.edge;

public interface IEdgeBuilder<E extends IEdge, F extends IEdgeFactory<E>, B extends IEdgeBuilder<E, F, B>> {

	E build();

	B source(String nodeId);

	B target(String nodeId);

	B setEdgeFactory(F factory);

	F getEdgeFactory();

}
