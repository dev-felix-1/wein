package de.fekl.dine.api.core;

public class SimpleNodeDeprecated implements INodeDeprecated {

	public static final SimpleNodeDeprecated PLACEHOLDER = new SimpleNodeDeprecated();

	@Override
	public String print() {
		return "SimpleNode";
	}

}
