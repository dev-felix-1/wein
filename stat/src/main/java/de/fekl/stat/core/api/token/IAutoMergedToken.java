package de.fekl.stat.core.api.token;

import java.util.List;

public interface IAutoMergedToken<T extends IToken> {

	List<T> getTokensBeforeMerge();
	

}
