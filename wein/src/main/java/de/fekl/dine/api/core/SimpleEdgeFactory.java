package de.fekl.dine.api.core;

public class SimpleEdgeFactory implements IEdgeFactory {

	@Override
	public IEdge createEdge(String source, String target) {
		return new SimpleEdge(source, target);
	}

}
