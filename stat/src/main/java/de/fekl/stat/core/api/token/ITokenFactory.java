package de.fekl.stat.core.api.token;

import java.util.List;

public interface ITokenFactory<T extends IToken> {

	T createToken(String id);

	default T createToken() {
		return createToken(TokenNames.generateTokenName());
	}

	T copyToken(T token);

	T mergeToken(List<T> tokens);

}
