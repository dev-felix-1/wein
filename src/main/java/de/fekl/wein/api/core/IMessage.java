package de.fekl.wein.api.core;

import de.fekl.dine.api.state.IToken;

public interface IMessage<T> extends IToken {

	T getValue();

}
