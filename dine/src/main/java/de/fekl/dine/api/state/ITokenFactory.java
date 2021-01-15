package de.fekl.dine.api.state;

public interface ITokenFactory<T extends IToken> {

	T createToken(String id);

	T copyToken(T token);

}
