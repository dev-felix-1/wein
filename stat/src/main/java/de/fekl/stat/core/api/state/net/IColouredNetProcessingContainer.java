package de.fekl.stat.core.api.state.net;

import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public interface IColouredNetProcessingContainer<T extends IToken> {

	void process(T token);

	ITokenStore<T> getCurrentState();

	void reset();
	
	boolean isRunning();
}
