package de.fekl.tore.api.core;

import java.util.HashMap;
import java.util.Map;

import de.fekl.tore.api.core.reader.TolerantMapReaderFactory;
import de.fekl.tore.api.core.reader.TolerantObjectReaderFactory;

@SuppressWarnings("unchecked")
public class TolerantReaderContext {

	public static final TolerantReaderContext DEFAULT = new TolerantReaderContext();
	@SuppressWarnings("rawtypes")
	public static final Map<Class, ITolerantReaderFactory> DEFAULT_FACTORIES = new HashMap<>();

	static {
		DEFAULT_FACTORIES.put(Object.class, new TolerantObjectReaderFactory());
		DEFAULT_FACTORIES.put(Map.class, new TolerantMapReaderFactory());

		DEFAULT_FACTORIES.forEach((k, v) -> DEFAULT.getRegistry().registerTolerantReaderFactory(k, v));
	}

	private TolerantReaderRegistry registry = new TolerantReaderRegistry(this);

	public TolerantReaderRegistry getRegistry() {
		return registry;
	}

	public <T, R extends ITolerantReaderFactory<T>> void registerTolerantReaderFactory(Class<T> clazz, R factory) {
		registry.registerTolerantReaderFactory(clazz, factory);
	}

	public <T> ITolerantReader createTolerantReader(T objectToRead) {
		ITolerantReaderFactory<Object> tolerantReaderFactory = (ITolerantReaderFactory<Object>) registry
				.getTolerantReaderFactory(objectToRead.getClass());
		return tolerantReaderFactory.createTolerantReader(objectToRead);
	}

}
