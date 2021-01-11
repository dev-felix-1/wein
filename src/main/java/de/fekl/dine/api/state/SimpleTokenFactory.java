package de.fekl.dine.api.state;

public class SimpleTokenFactory implements ITokenFactory<SimpleToken> {

	@Override
	public SimpleToken createToken(String id) {
		return new SimpleToken(id);
	}

	@Override
	public SimpleToken copyToken(IToken token) {
		return new SimpleToken(TokenNames.generateTokenName());
	}

}
