package de.fekl.dine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import de.fekl.dine.api.node.AbstractNodeBuilder;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.INodeFactory;
import de.fekl.dine.api.node.ISimpleNodeFactory;
import de.fekl.dine.api.node.NodeNames;
import de.fekl.dine.api.node.SimpleNode;
import de.fekl.dine.api.node.SimpleNodeBuilder;

public class NodeBuilderTest {

	// DEFAULT IMPL
	@Test
	public void testCreateValidSimpleNode() {
		INode node = new SimpleNodeBuilder().id("A").build();
		Assertions.assertNotNull(node);
		INode nodeWithGeneratedName = new SimpleNodeBuilder().build();
		Assertions.assertNotNull(nodeWithGeneratedName);
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void testCreateSimpleNodeWithEmptyAndNullId(String id) {
		Assertions.assertDoesNotThrow(() -> new SimpleNodeBuilder().id(id).build());
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
		public CustomNode build() {
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
		public ComplexCustomNode build() {
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
