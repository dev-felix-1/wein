package de.fekl.tran.api.core;

import java.util.List;

public interface IMerger<T> extends ITransformer<List<?>, T> {

	@Override
	default IMessage<T> transform(IMessage<List<?>> msg) {
		throw new UnsupportedOperationException("Merger does not support single-message transform.");
	}
	
	@Override
	default IContentType<List<?>> getSourceContentType() {
		throw new UnsupportedOperationException("Merger does not support single-message content type.");
	}
	
	IMessage<T> merge(List<IMessage<?>> messages);
	
	List<IContentType<?>> getSourceContentTypes();

}
