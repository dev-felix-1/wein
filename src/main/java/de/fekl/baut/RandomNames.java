package de.fekl.baut;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RandomNames {

	private RandomNames() {

	}

	private static final Map<String, Set<String>> NAMES = new ConcurrentHashMap<>();

	public static synchronized String getRandomName(String namespace, String prefix, int maxLength) {
		Set<String> names = NAMES.computeIfAbsent(namespace, k -> new HashSet<>());
		String rdmName;
		do {
			rdmName = (prefix + UUID.randomUUID().toString()).substring(0, maxLength);
		} while (names.contains(rdmName));
		return rdmName;
	}

}
