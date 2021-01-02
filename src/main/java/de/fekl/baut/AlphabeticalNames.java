package de.fekl.baut;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AlphabeticalNames {

	private AlphabeticalNames() {

	}

	private static final Map<String, List<String>> NAMES = new ConcurrentHashMap<>();
	private static final Map<String, AtomicInteger> LETTERS = new ConcurrentHashMap<>();

	public static synchronized String getNextName(String namespace) {
		throw new UnsupportedOperationException();
	}

	public static synchronized String getNextLetter(String namespace) {
		AtomicInteger currentLetter = LETTERS.computeIfAbsent(namespace, k -> new AtomicInteger((int) 'a' - 1));
		return Character.toString((char) currentLetter.incrementAndGet());

	}

}
