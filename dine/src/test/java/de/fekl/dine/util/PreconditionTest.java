package de.fekl.dine.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class PreconditionTest {

	@Test
	public void testHasClass() {
		assertDoesNotThrow(() -> Precondition.hasClass(this, getClass()));
		assertThrowsIllegalArgument(() -> Precondition.hasClass(this, String.class));
	}

	@Test
	public void testIsNotNull() {
		assertDoesNotThrow(() -> Precondition.isNotNull(this));
		assertDoesNotThrow(() -> Precondition.isNotNull(this, "isNull"));
		assertDoesNotThrow(() -> Precondition.isNotNull(this, "is%s", "Null"));

		assertThrowsIllegalArgument(() -> Precondition.isNotNull(null));
		assertThrowsIllegalArgument(() -> Precondition.isNotNull(null, "isNull"), "isNull");
		assertThrowsIllegalArgument(() -> Precondition.isNotNull(null, "is%s", "Null"), "isNull");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIsNotEmpty() {
		assertDoesNotThrow(() -> Precondition.isNotEmpty(this));
		assertDoesNotThrow(() -> Precondition.isNotEmpty(this, "isEmpty"));
		assertDoesNotThrow(() -> Precondition.isNotEmpty(this, "is%s", "Empty"));
		assertDoesNotThrow(() -> Precondition.isNotEmpty("a", "is%s", "Empty"));
		assertDoesNotThrow(() -> Precondition.isNotEmpty(Collections.singleton("a"), "is%s", "Empty"));

		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty((Object) null));
		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty(""));
		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty(Collections.EMPTY_LIST));

		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty((Object) null, "isEmpty"), "isEmpty");
		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty("", "isEmpty"), "isEmpty");
		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty(Collections.EMPTY_LIST, "isEmpty"), "isEmpty");

		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty((Object) null, "is%s", "Empty"), "isEmpty");
		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty("", "is%s", "Empty"), "isEmpty");
		assertThrowsIllegalArgument(() -> Precondition.isNotEmpty(Collections.EMPTY_LIST, "is%s", "Empty"), "isEmpty");
	}

	private static void assertThrowsIllegalArgument(Executable excecutable) {
		assertThrows(IllegalArgumentException.class, excecutable);
	}

	private static void assertThrowsIllegalArgument(Executable excecutable, String expectedMessage) {
		assertThrows(IllegalArgumentException.class, excecutable, expectedMessage);
	}

}
