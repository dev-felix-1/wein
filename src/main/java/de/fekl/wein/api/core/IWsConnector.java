package de.fekl.wein.api.core;

import de.fekl.dine.api.core.INodeDeprecated;

public interface IWsConnector<S, T> extends INodeDeprecated {

	IMessage<T> exchange(IMessage<S> msg);

}
