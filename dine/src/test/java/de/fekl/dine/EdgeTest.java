package de.fekl.dine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.SimpleEdge;

public class EdgeTest {

	@Test
	public void testCreateValidSimpleEdge() {
		IEdge createEdge = new SimpleEdge("A", "B");
		Assertions.assertNotNull(createEdge);
	}

	@ParameterizedTest
	@CsvSource({ ",", ",TEST", "TEST," })
	public void testCreateSimpleEdgeWithEmptyStrings(String src, String trgt) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge(src, trgt));
	}

	public void testCreateSimpleEdgeWithNullStrings() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge(null, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge(null, "TEST"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge("TEST", null));
	}
}
