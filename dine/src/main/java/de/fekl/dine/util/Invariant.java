package de.fekl.dine.util;

import java.util.Collection;

public class Invariant {

	private Invariant() {

	}

	public static void isNotNull(Object o) {
		if (o == null) {
			throw new IllegalStateException();
		}
	}

	public static void isNotNull(Object o, String message) {
		if (o == null) {
			throw new IllegalStateException(message);
		}
	}

	public static void isNotNull(Object o, String messageFormat, Object... args) {
		if (o == null) {
			throw new IllegalStateException(String.format(messageFormat, args));
		}
	}

	public static <T> void hasClass(Object o, Class<T> clazz) {
		if (clazz == null || o == null || !clazz.isAssignableFrom(o.getClass())) {
			throw new IllegalStateException();
		}
	}

	public static void isNotEmpty(String s) {
		if (s == null || s.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public static void isNotEmpty(String s, String message) {
		if (s == null || s.isEmpty()) {
			throw new IllegalStateException(message);
		}
	}

	public static void isNotEmpty(String s, String messageFormat, Object... args) {
		if (s == null || s.isEmpty()) {
			throw new IllegalStateException(String.format(messageFormat, args));
		}
	}

	public static <T> void isNotEmpty(Collection<T> c) {
		if (c == null || c.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public static <T> void isNotEmpty(Collection<T> c, String message) {
		if (c == null || c.isEmpty()) {
			throw new IllegalStateException(message);
		}
	}

	public static <T> void isNotEmpty(Collection<T> c, String messageFormat, Object... args) {
		if (c == null || c.isEmpty()) {
			throw new IllegalStateException(String.format(messageFormat, args));
		}
	}

	@SuppressWarnings("unchecked")
	public static void isNotEmpty(Object o) {
		if (o == null) {
			throw new IllegalStateException();
		}
		if (String.class.isAssignableFrom(o.getClass())) {
			isNotEmpty((String) o);
		}
		if (Collection.class.isAssignableFrom(o.getClass())) {
			isNotEmpty((Collection<Object>) o);
		}
	}

	@SuppressWarnings("unchecked")
	public static void isNotEmpty(Object o, String message) {
		if (o == null) {
			throw new IllegalStateException(message);
		}
		if (String.class.isAssignableFrom(o.getClass())) {
			isNotEmpty((String) o, message);
		}
		if (Collection.class.isAssignableFrom(o.getClass())) {
			isNotEmpty((Collection<Object>) o, message);
		}
	}

	@SuppressWarnings("unchecked")
	public static void isNotEmpty(Object o, String messageFormat, Object... args) {
		if (o == null) {
			throw new IllegalStateException(String.format(messageFormat, args));
		}
		if (String.class.isAssignableFrom(o.getClass())) {
			isNotEmpty((String) o, messageFormat, args);
		}
		if (Collection.class.isAssignableFrom(o.getClass())) {
			isNotEmpty((Collection<Object>) o, messageFormat, args);
		}
	}

}
