package de.fekl.dine.core.impl.node;

import de.fekl.dine.core.api.node.AbstractNodeBuilder;
import de.fekl.dine.core.api.node.INodeBuilder;

public class SimpleNodeBuilder extends AbstractNodeBuilder<SimpleNode, SimpleNodeFactory, SimpleNodeBuilder>
		implements INodeBuilder<SimpleNode, SimpleNodeFactory, SimpleNodeBuilder> {

	public SimpleNodeBuilder() {
		super();
		setNodeFactory(new SimpleNodeFactory());
	}

	@Override
	protected SimpleNode doBuild() {
		return getNodeFactory().createNode(getId());
	}
}
