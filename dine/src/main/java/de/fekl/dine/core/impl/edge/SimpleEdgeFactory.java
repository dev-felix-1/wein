package de.fekl.dine.core.impl.edge;

import de.fekl.dine.core.api.edge.ISimpleEdgeFactory;

public class SimpleEdgeFactory implements ISimpleEdgeFactory<SimpleEdge> {

	@Override
	public SimpleEdge createEdge(String source, String target) {
		return new SimpleEdge(source, target);
	}

}
