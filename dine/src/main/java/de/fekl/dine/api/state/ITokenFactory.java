package de.fekl.dine.api.state;

import java.util.List;

public interface ITokenFactory<T extends IToken> {

	T createToken(String id);

	T copyToken(T token);
	
	T mergeToken(List<T> tokens);

}
