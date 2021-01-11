package de.fekl.dine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.SimpleEdge;
import de.fekl.dine.api.core.SimpleEdgeFactory;

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

	// CUSTOM IMPL

	public static final class CustomEdge extends SimpleEdge implements IEdge {
		public CustomEdge(String source, String target) {
			super(source, target);
		}
	}

	@Test
	public void testCreateValidCustomEdge() {
		IEdge createEdge = new SimpleEdgeFactory().createEdge("A", "B", CustomEdge.class);
		Assertions.assertNotNull(createEdge);
	}

	@ParameterizedTest
	@CsvSource({ ",", ",TEST", "TEST," })
	public void testCreateCustomEdgeWithEmptyStrings(String src, String trgt) {
		Assertions.assertThrows(IllegalStateException.class,
				() -> new SimpleEdgeFactory().createEdge(src, trgt, CustomEdge.class));
	}

	public void testCreateCustomEdgeWithNullStrings() {
		Assertions.assertThrows(IllegalStateException.class,
				() -> new SimpleEdgeFactory().createEdge(null, null, CustomEdge.class));
		Assertions.assertThrows(IllegalStateException.class,
				() -> new SimpleEdgeFactory().createEdge(null, "TEST", CustomEdge.class));
		Assertions.assertThrows(IllegalStateException.class,
				() -> new SimpleEdgeFactory().createEdge("TEST", null, CustomEdge.class));
	}

}
