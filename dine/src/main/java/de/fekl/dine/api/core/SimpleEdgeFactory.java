package de.fekl.dine.api.core;

import de.fekl.baut.ReflectionUtils;

public class SimpleEdgeFactory implements IEdgeFactory {

	@Override
	public IEdge createEdge(String source, String target) {
		return new SimpleEdge(source, target);
	}

	@Override
	public <E extends IEdge> E createEdge(String source, String target, Class<E> clazz) {
		return ReflectionUtils.createInstanceUnsafe(clazz, source, target);
	}

}
