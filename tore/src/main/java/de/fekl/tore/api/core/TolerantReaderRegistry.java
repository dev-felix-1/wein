package de.fekl.tore.api.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TolerantReaderRegistry {

	private final TolerantReaderContext context;
	private final Map<Class<?>, ITolerantReaderFactory<?>> factories = new HashMap<>();

	public TolerantReaderRegistry(TolerantReaderContext context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public <T> ITolerantReaderFactory<T> getTolerantReaderFactory(Class<T> clazz) {
		Class<?> closestClass = findClosestClass(clazz, factories.keySet());
		if (closestClass != null) {
			return (ITolerantReaderFactory<T>) factories.get(closestClass);
		} else {
			return null;
		}
	}

	private static Class<?> findClosestClass(Class<?> clazz, Set<Class<?>> classes) {
		Class<?> result = null;
		Iterator<Class<?>> iterator = classes.iterator();
		while (iterator.hasNext()) {
			Class<?> it = iterator.next();
			if (it.isAssignableFrom(clazz)) {
				if (result == null || result.isAssignableFrom(it)) {
					result = it;
				}
			}
		}
		return result;
	}

	public <T, R extends ITolerantReaderFactory<T>> void registerTolerantReaderFactory(Class<T> clazz, R factory) {
		factory.setContext(context);
		factories.put(clazz, factory);
	}

}
