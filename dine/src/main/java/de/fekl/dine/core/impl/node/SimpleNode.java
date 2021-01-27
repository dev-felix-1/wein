package de.fekl.dine.core.impl.node;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.util.AbstractIdHolder;

public class SimpleNode extends AbstractIdHolder<String> implements INode {

	public SimpleNode(String id) {
		super(id);
	}

	@Override
	public String toString() {
		return String.format("SimpleNode(%s)", getId());
	}

	@Override
	public int hashCode() {
		return this.getClass().hashCode() + getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof INode) {
			INode node = (INode) obj;
			return getId().equals(node.getId());
		}
		return false;
	}

}
