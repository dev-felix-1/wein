package de.fekl.wein.api.core;

import de.fekl.cone.api.core.IToken;

public interface IMessage<T> extends IToken {

	T getValue();

}
