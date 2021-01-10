package de.fekl.dine.api.state;

public interface ITokenFactory {

	IToken createToken(String id);

	IToken copyToken(IToken token);

}
