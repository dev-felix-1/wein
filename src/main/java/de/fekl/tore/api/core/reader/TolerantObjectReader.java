package de.fekl.tore.api.core.reader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.fekl.tore.api.core.AbstractTolerantReader;
import de.fekl.tore.api.core.ITolerance;
import de.fekl.tore.api.core.Tolerance;
import de.fekl.tore.api.core.TolerantReaderContext;

public class TolerantObjectReader extends AbstractTolerantReader<Object> {

	public TolerantObjectReader(Object objectToRead) {
		super();
		setObjectToRead(objectToRead);
	}

	public TolerantObjectReader(Object objectToRead, TolerantReaderContext context) {
		super();
		setObjectToRead(objectToRead);
		setContext(context);
	}

	@Override
	public Integer getIntValue() {
		return Integer.parseInt(getStringValue());
	}

	@Override
	public Long getLongValue() {
		return Long.parseLong(getStringValue());
	}

	@Override
	public Boolean getBooleanValue() {
		return Boolean.valueOf(getStringValue());
	}

	@Override
	protected Object readPropertyRegular(String propertyname) {
		try {
			Method method = getObjectToRead().getClass().getMethod("get" + toFirstUpper(propertyname), new Class[] {});
			return method.invoke(getObjectToRead(), (Object[]) null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String toFirstUpper(String str) {
		String result = str;
		if (str != null) {
			if (str.length() > 0) {
				result = String.valueOf(str.charAt(0)).toUpperCase();
				if (str.length() > 1) {
					result += str.substring(1);
				}
			}
		}
		return result;
	}

	@Override
	protected Object readPropertyTolerant(String propertyname, List<ITolerance> tolerancesToApplyInOrder) {
		if (tolerancesToApplyInOrder.contains(Tolerance.CASE)) {
			try {
				List<Method> methods = Arrays.asList(getObjectToRead().getClass().getMethods());
				List<Method> matchingMethods = methods.stream()
						.filter(m -> m.getName().toLowerCase().equals("get" + propertyname.toLowerCase()))
						.collect(Collectors.toList());
				if (matchingMethods.size() == 1) {
					Method method = matchingMethods.get(0);
					return method.invoke(getObjectToRead(), (Object[]) null);
				}
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
