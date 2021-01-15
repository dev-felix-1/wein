package de.fekl.tran.api.core;

import java.util.List;

public interface ISplitter<S, T> extends ISingleInputConnector<S>, IMultiOutputConnector<T> {

	List<IMessage<T>> transform(IMessage<S> msg);

}
