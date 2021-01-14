package de.fekl.dine.api.node;

import de.fekl.dine.api.base.AbstractIdHolder;

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
		return this.getClass().hashCode() + getId().getBytes().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof INode node && getId().equals(node.getId());
	}

}
