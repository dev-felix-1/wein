package de.fekl.dine.core.impl.node;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.node.AbstractNodeBuilder;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.node.INodeFactory;
import de.fekl.dine.core.api.node.ISimpleNodeFactory;
import de.fekl.dine.core.api.node.NodeNames;

public class SimpleNodeBuilderTest {

	// DEFAULT IMPL
	@Test
	public void testCreateValidSimpleNode() {
		INode node = new SimpleNodeBuilder().id("A").build();
		Assertions.assertNotNull(node);
		INode nodeWithGeneratedName = new SimpleNodeBuilder().build();
		Assertions.assertNotNull(nodeWithGeneratedName);
	}

	@Test
	public void testCreateSimpleNodeWithEmptyAndNullId() {
		Assertions.assertDoesNotThrow(() -> new SimpleNodeBuilder().id(null).build());
		Assertions.assertDoesNotThrow(() -> new SimpleNodeBuilder().id("").build());
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

	public static final class CustomNodeBuilder
			extends AbstractNodeBuilder<CustomNode, CustomNodeFactory, CustomNodeBuilder> {

		CustomNodeBuilder() {
			setNodeFactory(new CustomNodeFactory());
		}

		@Override
		public CustomNode doBuild() {
			return getNodeFactory().createNode(getId());
		}

	}

	@Test
	public void testCreateValidCustomNode() {
		INode node = new CustomNodeBuilder().id("A").build();
		Assertions.assertNotNull(node);
		INode nodeWithGeneratedName = new CustomNodeBuilder().build();
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

	public static final class ComplexCustomNodeBuilder
			extends AbstractNodeBuilder<ComplexCustomNode, ComplexCustomNodeFactory, ComplexCustomNodeBuilder> {

		private String additionalProperty;

		public ComplexCustomNodeBuilder() {
			setNodeFactory(new ComplexCustomNodeFactory());
		}

		public ComplexCustomNodeBuilder additionalProperty(String additionalProperty) {
			this.additionalProperty = additionalProperty;
			return this;
		}

		@Override
		public ComplexCustomNode doBuild() {
			return getNodeFactory().createNode(getId(), additionalProperty);
		}

	}

	@Test
	public void testCreateValidComplexCustomNode() {
		ComplexCustomNode node = new ComplexCustomNodeBuilder().id("nodeIdValue")
				.additionalProperty("additionaPropValue").build();
		Assertions.assertNotNull(node);
		Assertions.assertEquals("nodeIdValue", node.getId());
		Assertions.assertEquals("additionaPropValue", node.getAdditionalProperty());
		INode nodeWithGeneratedName = new ComplexCustomNodeBuilder().additionalProperty("additionaPropValue").build();
		Assertions.assertNotNull(nodeWithGeneratedName);
		Assertions.assertEquals("additionaPropValue", node.getAdditionalProperty());
	}

}
