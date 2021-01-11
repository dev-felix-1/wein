package de.fekl.dine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.core.SimpleNode;
import de.fekl.dine.api.core.SimpleNodeFactory;

public class NodeFactoryTest {

	// DEFAULT IMPL
	@Test
	public void testCreateValidSimpleNode() {
		INode node = new SimpleNodeFactory().createNode("A");
		Assertions.assertNotNull(node);
		INode nodeWithGeneratedName = new SimpleNodeFactory().createNode();
		Assertions.assertNotNull(nodeWithGeneratedName);
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void testCreateSimpleNodeWithEmptyAndNullId(String id) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleNodeFactory().createNode(id));
	}

	// CUSTOM IMPL
	public static final class CustomNode extends SimpleNode implements INode {

		public CustomNode(String id) {
			super(id);
		}
	}

	@Test
	public void testCreateValidCustomNode() {
		INode node = new SimpleNodeFactory().createNode("A", CustomNode.class);
		Assertions.assertNotNull(node);
		INode nodeWithGeneratedName = new SimpleNodeFactory().createNode(CustomNode.class);
		Assertions.assertNotNull(nodeWithGeneratedName);
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void testCreateCustomNodeWithEmptyAndNullId(String id) {
		Assertions.assertThrows(IllegalStateException.class,
				() -> new SimpleNodeFactory().createNode(id, CustomNode.class));
	}

}
