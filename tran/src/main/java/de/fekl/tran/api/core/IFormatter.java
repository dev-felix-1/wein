package de.fekl.tran.api.core;

public interface IFormatter<T> extends ITransformer<T, T> {

	@Override
	default IContentType<T> getTargetContentType() {
		return getSourceContentType();
	}

}
