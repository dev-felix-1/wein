package de.fekl.tran.api.core;

import java.util.Arrays;
import java.util.List;

public interface ISingleOutputConnector<T> extends IOutputConnector<T> {

	@Override
	default List<IContentType<T>> getTargetContentTypes() {
		return Arrays.asList(getTargetContentType());
	}

	IContentType<T> getTargetContentType();

}
