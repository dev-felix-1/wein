package de.fekl.tran.api.core;

import java.util.List;

public interface IAutoMergedMessage extends IMessage<List<?>> {

	@Override
	default IContentType<List<?>> getContentType() {
		throw new UnsupportedOperationException("getContentType not supported for merged message");
	}
	
	List<IContentType<?>> getContentTypes();
}
