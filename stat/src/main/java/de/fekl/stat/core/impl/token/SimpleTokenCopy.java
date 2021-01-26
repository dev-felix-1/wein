package de.fekl.stat.core.impl.token;

import de.fekl.stat.core.api.token.ITokenCopy;

public class SimpleTokenCopy extends SimpleToken implements ITokenCopy<SimpleToken> {

	private SimpleToken original;

	public SimpleTokenCopy(String id, SimpleToken original) {
		super(id);
		this.original = original;
	}

	@Override
	public SimpleToken getOriginal() {
		return original;
	}

}
