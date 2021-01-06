package de.fekl.tore.api.core.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.fekl.tore.api.core.AbstractTolerantReader;
import de.fekl.tore.api.core.ITolerance;
import de.fekl.tore.api.core.Tolerance;
import de.fekl.tore.api.core.TolerantReaderContext;

public class TolerantMapReader extends AbstractTolerantReader<Map<String, Object>> {

	public TolerantMapReader(Map<String, Object> objectToRead) {
		super();
		setObjectToRead(objectToRead);
	}

	public TolerantMapReader(Map<String, Object> objectToRead, TolerantReaderContext context) {
		super();
		setObjectToRead(objectToRead);
		setContext(context);
	}

	@Override
	public Integer getIntValue() {
		throw new UnsupportedOperationException("Cannot cast a map to int");
	}

	@Override
	public Long getLongValue() {
		throw new UnsupportedOperationException("Cannot cast a map to long");
	}

	@Override
	public Boolean getBooleanValue() {
		throw new UnsupportedOperationException("Cannot cast a map to boolean");
	}

	@Override
	protected Object readPropertyRegular(String propertyname) {
		return getObjectToRead().get(propertyname);
	}

	@Override
	protected Object readPropertyTolerant(String propertyName, List<ITolerance> tolerancesToApplyInOrder) {
		if (tolerancesToApplyInOrder.contains(Tolerance.POSITION)
				&& tolerancesToApplyInOrder.contains(Tolerance.CASE)) {
			return readPropertyPositionAndCaseTolerant(propertyName);
		} else if (tolerancesToApplyInOrder.contains(Tolerance.CASE)) {
			return readPropertyCaseTolerant(propertyName);
		} else if (tolerancesToApplyInOrder.contains(Tolerance.POSITION)) {
			return readPropertyPositionTolerant(propertyName);
		}
		return null;
	}

	protected Object readPropertyPositionAndCaseTolerant(String propertyName) {
		List<Object> values = findPropertyValuesRecursively(getObjectToRead(), propertyName, true);
		if (values.size() == 1) {
			return values.get(0);
		}
		return null;
	}

	protected Object readPropertyCaseTolerant(String propertyName) {
		try {
			List<String> collect = getObjectToRead().keySet().stream()
					.filter(k -> k.toLowerCase().equals(propertyName.toLowerCase())).collect(Collectors.toList());
			if (collect.size() == 1) {
				return getObjectToRead().get(collect.get(0));
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Object readPropertyPositionTolerant(String propertyName) {
		List<Object> values = findPropertyValuesRecursively(getObjectToRead(), propertyName, false);
		if (values.size() == 1) {
			return values.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static List<Object> findPropertyValuesRecursively(Map<String, Object> map, String propertyName,
			boolean ignoreCase) {
		if (ignoreCase && !propertyName.toLowerCase().equals(propertyName)) {
			return findPropertyValuesRecursively(map, propertyName.toLowerCase(), ignoreCase);
		} else {
			List<Object> values = new ArrayList<>();
			map.entrySet().forEach(entry -> {
				if ((!ignoreCase && entry.getKey().equals(propertyName))
						|| (ignoreCase && entry.getKey().toLowerCase().equals(propertyName))) {
					values.add(entry.getValue());
				} else if (entry.getValue() instanceof Map) {
					values.addAll(findPropertyValuesRecursively((Map<String, Object>) entry.getValue(), propertyName,
							ignoreCase));
				}
			});
			return values;
		}
	}
}
