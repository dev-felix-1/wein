package de.fekl.stat.core.api.edge.conditional;


public interface ISimpleConditionalEdgeFactory<E extends IConditionalEdge> extends IConditionalEdgeFactory<E> {

	E createEdge(String source, String target, ICondition condition);

}
