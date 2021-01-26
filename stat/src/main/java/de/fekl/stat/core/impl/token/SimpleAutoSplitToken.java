package de.fekl.stat.core.impl.token;

import de.fekl.stat.core.api.token.IAutoSplitToken;

public class SimpleAutoSplitToken extends SimpleToken implements IAutoSplitToken<SimpleToken> {

	private SimpleToken tokenBeforeSplit;

	public SimpleAutoSplitToken(String id, SimpleToken tokenBeforeSplit) {
		super(id);
		this.tokenBeforeSplit = tokenBeforeSplit;
	}

	@Override
	public SimpleToken getTokenBeforeSplit() {
		return tokenBeforeSplit;
	}

}
