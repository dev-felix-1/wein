package de.fekl.dine.api.state;

public class SimpleTokenFactory implements ITokenFactory {

	@Override
	public IToken createToken(String id) {
		return new SimpleToken(id);
	}

	@Override
	public IToken copyToken(IToken token) {
		return new SimpleToken(TokenNames.generateTokenName());
	}

}
