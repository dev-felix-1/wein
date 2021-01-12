package de.fekl.dine.api.node;

public class SimpleNodeFactory implements INodeFactory<SimpleNode, INodeFactoryParams<SimpleNode>> {

	@Override
	public SimpleNode createNode(INodeFactoryParams<SimpleNode> params) {
		if (params.id() == null || params.id().isBlank()) {
			return new SimpleNode(NodeNames.generateNodeName());
		} else {
			return new SimpleNode(params.id());
		}
	}

}
