package de.fekl.dine.todo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.node.INodeFactory;
import de.fekl.dine.core.api.node.ISimpleNodeFactory;
import de.fekl.dine.core.api.node.NodeNames;
import de.fekl.dine.core.impl.node.SimpleNode;
import de.fekl.dine.core.impl.node.SimpleNodeFactory;

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
		Assertions.assertDoesNotThrow(() -> new SimpleNodeFactory().createNode(id));
	}

	// CUSTOM IMPL
	public static final class CustomNode extends SimpleNode implements INode {

		public CustomNode(String id) {
			super(id);
		}
	}

	public static final class CustomNodeFactory implements ISimpleNodeFactory<CustomNode> {
		@Override
		public CustomNode createNode(String id) {
			if (id == null || id.isBlank()) {
				return new CustomNode(NodeNames.generateNodeName());
			} else {
				return new CustomNode(id);
			}
		}
	}

	@Test
	public void testCreateValidCustomNode() {
		INode node = new CustomNodeFactory().createNode("A");
		Assertions.assertNotNull(node);
		INode nodeWithGeneratedName = new CustomNodeFactory().createNode();
		Assertions.assertNotNull(nodeWithGeneratedName);
	}

	// COMPLEX CUSTOM IMPL
	public static final class ComplexCustomNode extends SimpleNode implements INode {

		private final String additionalProperty;

		public ComplexCustomNode(String id, String additionalProperty) {
			super(id);
			this.additionalProperty = additionalProperty;
		}

		public String getAdditionalProperty() {
			return additionalProperty;
		}
	}

	public static final class ComplexCustomNodeFactory implements INodeFactory<ComplexCustomNode> {

		public ComplexCustomNode createNode(String id, String additionalProperty) {
			if (id == null || id.isBlank()) {
				return new ComplexCustomNode(NodeNames.generateNodeName(), id);
			} else {
				return new ComplexCustomNode(id, additionalProperty);
			}
		}

	}

	@Test
	public void testCreateValidComplexCustomNode() {
		ComplexCustomNode node = new ComplexCustomNodeFactory().createNode("nodeIdValue", "additionaPropValue");
		Assertions.assertNotNull(node);
		Assertions.assertEquals("nodeIdValue", node.getId());
		Assertions.assertEquals("additionaPropValue", node.getAdditionalProperty());
		INode nodeWithGeneratedName = new ComplexCustomNodeFactory().createNode(null, "additionaPropValue");
		Assertions.assertNotNull(nodeWithGeneratedName);
		Assertions.assertEquals("additionaPropValue", node.getAdditionalProperty());
	}

}
