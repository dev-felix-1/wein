package de.fekl.dine.api.core;

import de.fekl.baut.Precondition;

public class SimpleNode implements INode {

	private final String id;

	public SimpleNode(String id) {
		Precondition.isNotEmpty(id);
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("SimpleNode(%s)", id);
	}

	@Override
	public int hashCode() {
		return this.getClass().hashCode() + id.getBytes().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof INode node && getId().equals(node.getId());
	}

}
