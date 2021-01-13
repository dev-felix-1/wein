package de.fekl.dine.api.edge;

public interface ISimpleEdgeFactory<E extends IEdge> extends IEdgeFactory<E> {

	E createEdge(String source, String target);

}
