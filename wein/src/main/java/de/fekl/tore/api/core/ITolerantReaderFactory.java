package de.fekl.tore.api.core;

public interface ITolerantReaderFactory<T> {

	ITolerantReader createTolerantReader(T objectToRead);

	void setContext(TolerantReaderContext context);

}
