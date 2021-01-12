package de.fekl.dine.api.node;

public class SimpleNodeBuilder extends AbstractNodeBuilder<SimpleNode, SimpleNodeBuilder>
		implements INodeBuilder<SimpleNode, SimpleNodeBuilder> {

	public SimpleNodeBuilder() {
		super();
		setNodeFactory(new SimpleNodeFactory());
	}

	@Override
	public SimpleNode build() {
		return getFactory().createNode(new SimpleNodeFactoryParams<SimpleNode>(getId()));
	}

}
