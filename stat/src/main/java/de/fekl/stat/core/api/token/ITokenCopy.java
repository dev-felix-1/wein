package de.fekl.stat.core.api.token;

public interface ITokenCopy<T extends IToken> extends IToken {

	T getOriginal();
}
