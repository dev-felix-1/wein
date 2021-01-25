package de.fekl.dine.core.impl.edge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.edge.IEdge;

public class SimpleEdgeFactoryTest {

	// DEFAULT IMPL
	@Test
	public void testCreateValidSimpleEdge() {
		IEdge createEdge = new SimpleEdgeFactory().createEdge("A", "B");
		Assertions.assertNotNull(createEdge);
	}

	@Test
	public void testCreateSimpleEdgeWithEmptyStrings() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge("", ""));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge("", "TEST"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge("TEST", ""));
	}

	public void testCreateSimpleEdgeWithNullStrings() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge(null, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge(null, "TEST"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge("TEST", null));
	}

}
