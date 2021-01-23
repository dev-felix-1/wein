package de.fekl.dine.core.api.edge;

public abstract class AbstractEdgeBuilder<E extends IEdge, F extends IEdgeFactory<E>, B extends AbstractEdgeBuilder<E, F, B>>
		implements IEdgeBuilder<E, F, B> {

	private String sourceNodeId;
	private String targetNodeId;
	private F factory;

	@SuppressWarnings("unchecked")
	@Override
	public B source(String nodeId) {
		sourceNodeId = nodeId;
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public B target(String nodeId) {
		targetNodeId = nodeId;
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public B setEdgeFactory(F factory) {
		this.factory = factory;
		return (B) this;
	}

	protected String getSourceNodeId() {
		return sourceNodeId;
	}

	protected String getTargetNodeId() {
		return targetNodeId;
	}

	@Override
	public F getEdgeFactory() {
		return factory;
	}
}
