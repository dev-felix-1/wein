package de.fekl.baut;

import java.util.Collection;

public class Precondition {

	private Precondition() {

	}

	public static void isNotNull(Object o) {
		if (o == null) {
			throw new IllegalArgumentException();
		}
	}

	public static void isNotNull(Object o, String message) {
		if (o == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNotNull(Object o, String messageFormat, Object... args) {
		if (o == null) {
			throw new IllegalArgumentException(String.format(messageFormat, args));
		}
	}

	public static <T> void hasClass(Object o, Class<T> clazz) {
		if (clazz == null || o == null || !clazz.isAssignableFrom(o.getClass())) {
			throw new IllegalArgumentException();
		}
	}

	public static void isNotEmpty(String s) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException();
		}
	}

	public static void isNotEmpty(String s, String message) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNotEmpty(String s, String messageFormat, Object... args) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException(String.format(messageFormat, args));
		}
	}

	public static <T> void isNotEmpty(Collection<T> c) {
		if (c == null || c.isEmpty()) {
			throw new IllegalArgumentException();
		}
	}

	public static <T> void isNotEmpty(Collection<T> c, String message) {
		if (c == null || c.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

	public static <T> void isNotEmpty(Collection<T> c, String messageFormat, Object... args) {
		if (c == null || c.isEmpty()) {
			throw new IllegalArgumentException(String.format(messageFormat, args));
		}
	}

	public static void isNotEmpty(Object o) {
		if (o == null) {
			throw new IllegalArgumentException();
		}
	}

	public static void isNotEmpty(Object o, String message) {
		if (o == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNotEmpty(Object o, String messageFormat, Object... args) {
		if (o == null) {
			throw new IllegalArgumentException(String.format(messageFormat, args));
		}
	}

}
