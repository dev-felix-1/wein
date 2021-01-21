package de.fekl.stat.core.impl.edge.conditional;

import de.fekl.stat.core.api.edge.conditional.ICondition;
import de.fekl.stat.core.api.edge.conditional.ISimpleConditionalEdgeFactory;

public class SimpleConditionalEdgeFactory implements ISimpleConditionalEdgeFactory<SimpleConditionalEdge> {

	@Override
	public SimpleConditionalEdge createEdge(String source, String target, ICondition condition) {
		return new SimpleConditionalEdge(source, target, condition);
	}

}
