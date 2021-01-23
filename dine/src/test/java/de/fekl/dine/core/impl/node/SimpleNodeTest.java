package de.fekl.dine.core.impl.node;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.node.INode;

public class SimpleNodeTest {

	@Test
	public void testCreateValidSimpleNode() {
		INode node = new SimpleNode("A");
		Assertions.assertNotNull(node);
	}

	@Test
	public void testCreateSimpleNodeWithEmptyAndNullId() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleNode(null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleNode(""));
	}
	
	@Test
	public void testEquals() {
		var node = new SimpleNode("A");
		Assertions.assertEquals(node, node);
		Assertions.assertEquals(node, new SimpleNode("A"));
		Assertions.assertNotEquals(node, new SimpleNode("B"));
		Assertions.assertNotEquals(node, null);
	}
	
	@Test
	public void testHashcode() {
		var node = new SimpleNode("A");
		Assertions.assertEquals(node.hashCode(), node.hashCode());
		Assertions.assertEquals(node.hashCode(), new SimpleNode("A").hashCode());
		Assertions.assertNotEquals(node.hashCode(), new SimpleNode("B").hashCode());
	}

}
