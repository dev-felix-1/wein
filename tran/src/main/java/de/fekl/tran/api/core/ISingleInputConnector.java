package de.fekl.tran.api.core;

import java.util.Arrays;
import java.util.List;

public interface ISingleInputConnector<S> extends IInputConnector<S> {

	@Override
	default List<IContentType<S>> getSourceContentTypes() {
		return Arrays.asList(getSourceContentType());
	}

	IContentType<S> getSourceContentType();
}
