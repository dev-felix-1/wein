package de.fekl.tran.api.core;

import java.util.List;

public interface IInputConnector<S> {

	List<IContentType<S>> getSourceContentTypes();
}
