package de.fekl.dine.api.state;

public class SimpleToken implements IToken {

	private final String id;

	public SimpleToken(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("%s('%s')", getClass().getSimpleName(), id);
	}

}
