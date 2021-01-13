package de.fekl.dine.api.edge;

public class SimpleEdgeFactory implements ISimpleEdgeFactory<SimpleEdge> {

	@Override
	public SimpleEdge createEdge(String source, String target) {
		return new SimpleEdge(source, target);
	}

}
