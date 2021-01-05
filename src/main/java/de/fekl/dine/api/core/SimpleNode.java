package de.fekl.dine.api.core;

import de.fekl.dine.api.graph.INode;

public class SimpleNode implements INode {

	private final String id;

	public SimpleNode(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
