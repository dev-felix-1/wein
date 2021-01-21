package de.fekl.dine.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class InvariantTest {

	@Test
	public void testHasClass() {
		assertDoesNotThrow(() -> Invariant.hasClass(this, getClass()));
		assertThrowsIllegalState(() -> Invariant.hasClass(this, String.class));
	}

	@Test
	public void testIsNotNull() {
		assertDoesNotThrow(() -> Invariant.isNotNull(this));
		assertDoesNotThrow(() -> Invariant.isNotNull(this, "isNull"));
		assertDoesNotThrow(() -> Invariant.isNotNull(this, "is%s", "Null"));

		assertThrowsIllegalState(() -> Invariant.isNotNull(null));
		assertThrowsIllegalState(() -> Invariant.isNotNull(null, "isNull"), "isNull");
		assertThrowsIllegalState(() -> Invariant.isNotNull(null, "is%s", "Null"), "isNull");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIsNotEmpty() {
		assertDoesNotThrow(() -> Invariant.isNotEmpty(this));
		assertDoesNotThrow(() -> Invariant.isNotEmpty(this, "isEmpty"));
		assertDoesNotThrow(() -> Invariant.isNotEmpty(this, "is%s", "Empty"));
		assertDoesNotThrow(() -> Invariant.isNotEmpty("a", "is%s", "Empty"));
		assertDoesNotThrow(() -> Invariant.isNotEmpty(Collections.singleton("a"), "is%s", "Empty"));

		assertThrowsIllegalState(() -> Invariant.isNotEmpty((Object) null));
		assertThrowsIllegalState(() -> Invariant.isNotEmpty(""));
		assertThrowsIllegalState(() -> Invariant.isNotEmpty(Collections.EMPTY_LIST));

		assertThrowsIllegalState(() -> Invariant.isNotEmpty((Object) null, "isEmpty"), "isEmpty");
		assertThrowsIllegalState(() -> Invariant.isNotEmpty("", "isEmpty"), "isEmpty");
		assertThrowsIllegalState(() -> Invariant.isNotEmpty(Collections.EMPTY_LIST, "isEmpty"), "isEmpty");

		assertThrowsIllegalState(() -> Invariant.isNotEmpty((Object) null, "is%s", "Empty"), "isEmpty");
		assertThrowsIllegalState(() -> Invariant.isNotEmpty("", "is%s", "Empty"), "isEmpty");
		assertThrowsIllegalState(() -> Invariant.isNotEmpty(Collections.EMPTY_LIST, "is%s", "Empty"), "isEmpty");
	}

	private static void assertThrowsIllegalState(Executable excecutable) {
		assertThrows(IllegalStateException.class, excecutable);
	}

	private static void assertThrowsIllegalState(Executable excecutable, String expectedMessage) {
		assertThrows(IllegalStateException.class, excecutable, expectedMessage);
	}

}
