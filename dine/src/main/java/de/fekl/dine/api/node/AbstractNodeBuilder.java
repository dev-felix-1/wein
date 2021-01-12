package de.fekl.dine.api.node;

public abstract class AbstractNodeBuilder<N extends INode, A extends AbstractNodeBuilder<N, A>>
		implements INodeBuilder<N, A> {

	private String id;
	private INodeFactory<N, INodeFactoryParams<N>> factory;

	@SuppressWarnings("unchecked")
	@Override
	public A id(String id) {
		this.id = id;
		return (A) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P extends INodeFactoryParams<N>, F extends INodeFactory<N, P>> A setNodeFactory(F factory) {
		this.factory = (INodeFactory<N, INodeFactoryParams<N>>) factory;
		return (A) this;
	}

	protected String getId() {
		return id;
	}

	protected INodeFactory<N, INodeFactoryParams<N>> getFactory() {
		return factory;
	}

}
