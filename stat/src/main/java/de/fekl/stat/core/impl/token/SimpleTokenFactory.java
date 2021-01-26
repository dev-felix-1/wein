package de.fekl.stat.core.impl.token;

import java.util.List;
import java.util.stream.Collectors;

import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.TokenNames;

public class SimpleTokenFactory implements ITokenFactory<SimpleToken> {

	@Override
	public SimpleToken createToken(String id) {
		return new SimpleToken(id);
	}

	@Override
	public SimpleToken copyToken(SimpleToken token) {
		return new SimpleTokenCopy(TokenNames.generateTokenName() + "_copy:" + token.getId(), token);
	}

	@Override
	public SimpleToken mergeToken(List<SimpleToken> tokens) {
		if (tokens.size() == 1) {
			return tokens.get(0);
		}
		return new SimpleAutoMergedToken(tokens.stream().map(IToken::getId).collect(Collectors.joining("+")), tokens);
	}

}
