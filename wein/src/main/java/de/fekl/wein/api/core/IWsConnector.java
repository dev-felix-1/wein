package de.fekl.wein.api.core;

import de.fekl.dine.api.node.INode;
import de.fekl.tran.api.core.IMessage;

public interface IWsConnector<S, T> extends INode {

	IMessage<T> exchange(IMessage<S> msg);

}
