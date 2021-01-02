package de.fekl.dine.api.core;

public class SimpleNode implements INode {

	public static final SimpleNode PLACEHOLDER = new SimpleNode();

	@Override
	public String print() {
		return "SimpleNode";
	}

}
