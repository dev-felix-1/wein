package de.fekl.stat.core.impl.node;

import de.fekl.dine.core.impl.node.SimpleNode;
import de.fekl.stat.core.api.node.IAutoMergeNode;
import de.fekl.stat.core.api.token.IToken;

public class SimpleAutoMergeNode<T extends IToken> extends SimpleNode implements IAutoMergeNode {

	public SimpleAutoMergeNode(String id) {
		super(id);
	}

}
