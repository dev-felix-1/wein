package de.fekl.stat.core.impl.token;

import java.util.List;

import de.fekl.stat.core.api.token.IAutoMergedToken;

public class SimpleAutoMergedToken extends SimpleToken implements IAutoMergedToken<SimpleToken> {

	private List<SimpleToken> tokensBeforeMerge;

	public SimpleAutoMergedToken(String id, List<SimpleToken> tokensBeforeMerge) {
		super(id);
		this.tokensBeforeMerge = tokensBeforeMerge;
	}

	@Override
	public List<SimpleToken> getTokensBeforeMerge() {
		return tokensBeforeMerge;
	}

}
