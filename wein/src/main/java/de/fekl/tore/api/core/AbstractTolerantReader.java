package de.fekl.tore.api.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.tore.api.core.reader.TolerantObjectReaderFactory;

public abstract class AbstractTolerantReader<T> implements ITolerantReader {

	private T objectToRead;
	private TolerantReaderContext context;

	private final Set<ITolerance> tolerances = new HashSet<>();
	private final Map<ITolerance, Integer> tolerancePriorities = new HashMap<>();

	protected void setObjectToRead(T objectToRead) {
		this.objectToRead = objectToRead;
	}

	protected void setContext(TolerantReaderContext context) {
		this.context = context;
	}

	protected T getObjectToRead() {
		return objectToRead;
	}

	protected TolerantReaderContext getContext() {
		return context;
	}

	@Override
	public ITolerantReader getProperty(String propertyName) {
		Object object = readPropertyRegular(propertyName);
		if (object == null) {
			object = readPropertyTolerant(propertyName);
		}
		return wrapUp(object);
	}

	@Override
	public void configure(ITolerance tolerance, boolean enabled) {
		if (enabled) {
			tolerances.add(tolerance);
			tolerancePriorities.putIfAbsent(tolerance, 0);
		} else {
			tolerances.remove(tolerance);
		}
	}

	@Override
	public void configure(ITolerance tolerance, int priority) {
		tolerancePriorities.put(tolerance, priority);
	}

	@Override
	public Object getValue() {
		return objectToRead;
	}

	@Override
	public String getStringValue() {
		if (getValue() != null) {
			return getValue().toString();
		} else {
			return null;
		}
	}

	protected abstract Object readPropertyRegular(String propertyname);

	protected final List<Integer> getSortedPriorities() {
		Map<Integer, List<Entry<ITolerance, Integer>>> priorities = tolerancePriorities.entrySet().stream()
				.collect(Collectors.groupingBy(e -> e.getValue()));
		List<Integer> priorityValues = new ArrayList<>(priorities.keySet());
		Collections.sort(priorityValues);
		return priorityValues;
	}

	protected final Object readPropertyTolerant(String propertyname) {
		List<Integer> sortedPriorities = getSortedPriorities();
		List<ITolerance> tolerancesToApplyInOrder = new ArrayList<>();
		for (Integer priorityValue : sortedPriorities) {
			List<ITolerance> tolerances = tolerancePriorities.entrySet().stream()
					.filter(e -> e.getValue().equals(priorityValue)).map(e -> e.getKey()).collect(Collectors.toList());
			tolerances.retainAll(this.tolerances);
			tolerancesToApplyInOrder.addAll(tolerances);
			Object tolerantReadProperty = readPropertyTolerant(propertyname, tolerancesToApplyInOrder);
			if (tolerantReadProperty != null) {
				return tolerantReadProperty;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected final <F> ITolerantReaderFactory<F> getRegisteredFactory(Class<F> clazz) {
		if (getContext() == null || getContext().getRegistry() == null) {
			return (ITolerantReaderFactory<F>) new TolerantObjectReaderFactory();
		}
		return getContext().getRegistry().getTolerantReaderFactory(clazz);
	}

	@SuppressWarnings("unchecked")
	protected final ITolerantReaderFactory<Object> getRegisteredFactory(Object value) {
		if (value == null) {
			return getRegisteredFactory(Object.class);
		} else {
			return (ITolerantReaderFactory<Object>) getRegisteredFactory(value.getClass());
		}
	}

	protected abstract Object readPropertyTolerant(String propertyname, List<ITolerance> tolerancesToApplyInOrder);

	protected ITolerantReader wrapUp(Object value) {
		ITolerantReaderFactory<Object> registeredFactory = getRegisteredFactory(value);
		return registeredFactory.createTolerantReader(value);
	}
}
