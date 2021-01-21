package de.fekl.tran.api.core;

import de.fekl.stat.core.api.token.IToken;

public interface IMessageContainer extends IToken {

	<T> void setMessage(IMessage<T> message);

	<T> IMessage<T> getMessage();
}
