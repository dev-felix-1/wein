package de.fekl.dine.api.core;

public interface IEdgeFactory {

	IEdge createEdge(String source, String target);

	<E extends IEdge> E createEdge(String source, String target, Class<E> clazz);

}
