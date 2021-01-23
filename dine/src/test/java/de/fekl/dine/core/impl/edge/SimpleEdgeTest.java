package de.fekl.dine.core.impl.edge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.edge.IEdge;

public class SimpleEdgeTest {

	@Test
	public void testCreateValidSimpleEdge() {
		IEdge createEdge = new SimpleEdge("A", "B");
		Assertions.assertNotNull(createEdge);
	}

	@Test
	public void testCreateSimpleEdgeWithEmptyStrings() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge("A", ""));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge("", "B"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge("", ""));
	}

	public void testCreateSimpleEdgeWithNullStrings() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge(null, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge(null, "TEST"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdge("TEST", null));
	}
}
