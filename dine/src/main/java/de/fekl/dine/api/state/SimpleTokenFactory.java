package de.fekl.dine.api.state;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleTokenFactory implements ITokenFactory<SimpleToken> {

	@Override
	public SimpleToken createToken(String id) {
		return new SimpleToken(id);
	}

	@Override
	public SimpleToken copyToken(SimpleToken token) {
		return createToken(TokenNames.generateTokenName());
	}

	@Override
	public SimpleToken mergeToken(List<SimpleToken> tokens) {
		return createToken(tokens.stream().map(IToken::getId).collect(Collectors.joining("+")));
	}

}
