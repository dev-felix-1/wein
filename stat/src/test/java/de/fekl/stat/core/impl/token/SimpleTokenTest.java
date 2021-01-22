package de.fekl.stat.core.impl.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTokenTest {

	@Test
	public void testConstruction() {
		Assertions.assertDoesNotThrow(() -> new SimpleToken("tid"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleToken(null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleToken(""));
	}

	@Test
	public void testEquals() {
		Assertions.assertEquals(new SimpleToken("tid"), new SimpleToken("tid"));
		Assertions.assertNotEquals(new SimpleToken("tid"), new SimpleToken("tid2"));
	}

}
