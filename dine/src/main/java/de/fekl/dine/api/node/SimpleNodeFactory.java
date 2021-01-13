package de.fekl.dine.api.node;

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
