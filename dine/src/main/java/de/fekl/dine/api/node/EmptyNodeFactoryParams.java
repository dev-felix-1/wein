package de.fekl.dine.api.node;

public record EmptyNodeFactoryParams<N extends INode> () implements INodeFactoryParams<N> {

	@Override
	public String id() {
		return null;
	}

}
