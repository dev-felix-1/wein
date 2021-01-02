package de.fekl.wein.api.core;

import de.fekl.dine.api.core.INode;

public interface IWsConnector<S, T> extends INode {

	IMessage<T> exchange(IMessage<S> msg);

}
