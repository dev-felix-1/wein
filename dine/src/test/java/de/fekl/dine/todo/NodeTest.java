package de.fekl.dine.todo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.impl.node.SimpleNode;

public class NodeTest {

	@Test
	public void testCreateValidSimpleNode() {
		INode node = new SimpleNode("A");
		Assertions.assertNotNull(node);
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void testCreateSimpleNodeWithEmptyAndNullId(String id) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleNode(id));
	}

}
