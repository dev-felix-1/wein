package de.fekl.dine.core.impl.node;

import de.fekl.dine.core.api.node.ISimpleNodeFactory;
import de.fekl.dine.core.api.node.NodeNames;

public class SimpleNodeFactory implements ISimpleNodeFactory<SimpleNode> {

	@Override
	public SimpleNode createNode(String id) {
		if (id == null || id.isBlank()) {
			return new SimpleNode(NodeNames.generateNodeName());
		} else {
			return new SimpleNode(id);
		}
	}

}
