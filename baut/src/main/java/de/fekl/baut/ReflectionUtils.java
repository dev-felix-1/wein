package de.fekl.baut;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

	private ReflectionUtils() {

	}

	public static <T> T createInstanceUnsafe(Class<T> clazz, Object... args) {
		try {
			List<Object> argumentList = Arrays.asList(args);
			List<Class<?>> collect = argumentList.stream().map(ReflectionUtils::induceType)
					.collect(Collectors.toList());
			Constructor<T> idConstructor = clazz.getConstructor(collect.toArray(new Class[] {}));
			return idConstructor.newInstance(args);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	private static Class<?> induceType(Object o) {
		if (o == null) {
			return Object.class;
		} else {
			return o.getClass();
		}
	}

}
