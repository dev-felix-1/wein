package de.fekl.dine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.edge.SimpleEdgeFactory;

public class EdgeFactoryTest {

	// DEFAULT IMPL
	@Test
	public void testCreateValidSimpleEdge() {
		IEdge createEdge = new SimpleEdgeFactory().createEdge("A", "B");
		Assertions.assertNotNull(createEdge);
	}

	@ParameterizedTest
	@CsvSource({ ",", ",TEST", "TEST," })
	public void testCreateSimpleEdgeWithEmptyStrings(String src, String trgt) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge(src, trgt));
	}

	public void testCreateSimpleEdgeWithNullStrings() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge(null, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge(null, "TEST"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleEdgeFactory().createEdge("TEST", null));
	}

}
