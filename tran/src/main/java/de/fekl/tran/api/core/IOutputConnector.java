package de.fekl.tran.api.core;

import java.util.List;

public interface IOutputConnector<T> {

	List<IContentType<T>> getTargetContentTypes();
}
