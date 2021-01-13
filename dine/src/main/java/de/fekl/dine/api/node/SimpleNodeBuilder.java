package de.fekl.dine.api.node;

public class SimpleNodeBuilder extends AbstractNodeBuilder<SimpleNode, SimpleNodeFactory, SimpleNodeBuilder>
		implements INodeBuilder<SimpleNode, SimpleNodeFactory, SimpleNodeBuilder> {

	public SimpleNodeBuilder() {
		super();
		setNodeFactory(new SimpleNodeFactory());
	}

	@Override
	public SimpleNode build() {
		return getNodeFactory().createNode(getId());
	}
}
