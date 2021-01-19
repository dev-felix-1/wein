package de.fekl.dine.api.edge.conditional;

public class SimpleConditionalEdgeFactory implements ISimpleConditionalEdgeFactory<SimpleConditionalEdge> {

	@Override
	public SimpleConditionalEdge createEdge(String source, String target, ICondition condition) {
		return new SimpleConditionalEdge(source, target, condition);
	}

}
