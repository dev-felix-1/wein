package de.fekl.tore.api.core;

import de.fekl.tore.api.core.reader.TolerantObjectReaderFactory;

public class TolerantReaderContext {

	public static final TolerantReaderContext DEFAULT;
	static {
		DEFAULT = new TolerantReaderContext();
		DEFAULT.getRegistry().setContext(DEFAULT);
		DEFAULT.getRegistry().registerTolerantReaderFactory(Object.class, new TolerantObjectReaderFactory());
	}

	private TolerantReaderRegistry registry = new TolerantReaderRegistry();

	public TolerantReaderRegistry getRegistry() {
		return registry;
	}

	public <T, R extends ITolerantReaderFactory<T>> void registerTolerantReaderFactory(Class<T> clazz, R factory) {
		registry.registerTolerantReaderFactory(clazz, factory);
	}

	public <T> ITolerantReader createTolerantReader(T objectToRead) {
		@SuppressWarnings("unchecked")
		ITolerantReaderFactory<Object> tolerantReaderFactory = (ITolerantReaderFactory<Object>) registry
				.getTolerantReaderFactory(objectToRead.getClass());
		return tolerantReaderFactory.createTolerantReader(objectToRead);
	}

}
