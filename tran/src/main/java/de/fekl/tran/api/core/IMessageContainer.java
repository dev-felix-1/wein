package de.fekl.tran.api.core;

import de.fekl.dine.api.state.IToken;

public interface IMessageContainer extends IToken {

	<T> void setMessage(IMessage<T> message);

	<T> IMessage<T> getMessage();
}
