package de.fekl.wein.api.core;

import de.fekl.cone.api.core.ITokenDeprecated;

public interface IMessage<T> extends ITokenDeprecated {

	T getValue();

}
