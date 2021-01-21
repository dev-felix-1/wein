package de.fekl.dine.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RandomNames {

	private RandomNames() {

	}

	private static final Map<String, Set<String>> NAMES = new ConcurrentHashMap<>();

	public static String DEFAULT_NAMESPACE = "DEFAULT_NAMESPACE";
	public static String DEFAULT_PREFIX = "";
	public static int DEFAULT_MAX_LENGTH = 30;

	public static synchronized String getRandomName(String namespace, String prefix, Integer maxLength) {
		if (prefix == null) {
			prefix = "";
		}
		if (namespace == null) {
			namespace = DEFAULT_NAMESPACE;
		}
		if (maxLength == null) {
			maxLength = DEFAULT_MAX_LENGTH;
		}
		Set<String> names = NAMES.computeIfAbsent(namespace, k -> new HashSet<>());
		String rdmName;
		do {
			rdmName = (prefix + UUID.randomUUID().toString()).substring(0, maxLength);
		} while (names.contains(rdmName));
		return rdmName;
	}

	public static synchronized String getRandomName() {
		return getRandomName(null, null, null);
	}

}
