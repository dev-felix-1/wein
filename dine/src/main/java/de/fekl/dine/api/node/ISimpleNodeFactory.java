package de.fekl.dine.api.node;

public interface ISimpleNodeFactory<N extends INode> extends INodeFactory<N> {

	default N createNode() {
		return createNode(null);
	}

	N createNode(String id);

}
