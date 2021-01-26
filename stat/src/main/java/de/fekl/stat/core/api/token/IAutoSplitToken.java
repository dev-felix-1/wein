package de.fekl.stat.core.api.token;

public interface IAutoSplitToken<T extends IToken> {

	T getTokenBeforeSplit();

}
