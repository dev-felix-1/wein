package de.fekl.dine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import de.fekl.dine.api.node.AbstractNodeFactory;
import de.fekl.dine.api.node.EmptyNodeFactoryParams;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.INodeFactoryParams;
import de.fekl.dine.api.node.NodeNames;
import de.fekl.dine.api.node.SimpleNode;
import de.fekl.dine.api.node.SimpleNodeFactory;
import de.fekl.dine.api.node.SimpleNodeFactoryParams;

public class NodeFactoryTest {

	// DEFAULT IMPL
	@Test
	public void testCreateValidSimpleNode() {
		INode node = new SimpleNodeFactory().createNode(new SimpleNodeFactoryParams<SimpleNode>("A"));
		Assertions.assertNotNull(node);
		INode nodeWithGeneratedName = new SimpleNodeFactory().createNode(new EmptyNodeFactoryParams<SimpleNode>());
		Assertions.assertNotNull(nodeWithGeneratedName);
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void testCreateSimpleNodeWithEmptyAndNullId(String id) {
		Assertions.assertDoesNotThrow(
				() -> new SimpleNodeFactory().createNode(new SimpleNodeFactoryParams<SimpleNode>(id)));
	}

	// CUSTOM IMPL
	public static final class CustomNode extends SimpleNode implements INode {

		public CustomNode(String id) {
			super(id);
		}
	}

	public static final class CustomNodeFactory
			extends AbstractNodeFactory<CustomNode, SimpleNodeFactoryParams<CustomNode>> {

		@Override
		public CustomNode createNode(SimpleNodeFactoryParams<CustomNode> params) {
			if (params.id() == null || params.id().isBlank()) {
				return new CustomNode(NodeNames.generateNodeName());
			} else {
				return new CustomNode(params.id());
			}
		}

	}

	@Test
	public void testCreateValidCustomNode() {
		INode node = new CustomNodeFactory().createNode(new SimpleNodeFactoryParams<CustomNode>("A"));
		Assertions.assertNotNull(node);
		INode nodeWithGeneratedName = new CustomNodeFactory().createNode(new SimpleNodeFactoryParams<CustomNode>(null));
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

	public static final class ComplexCustomNodeFactory
			extends AbstractNodeFactory<ComplexCustomNode, IComplexCustomNodeFactoryParams> {

		@Override
		public ComplexCustomNode createNode(IComplexCustomNodeFactoryParams params) {
			if (params.id() == null || params.id().isBlank()) {
				return new ComplexCustomNode(NodeNames.generateNodeName(), params.additionalProperty());
			} else {
				return new ComplexCustomNode(params.id(), params.additionalProperty());
			}
		}

	}

	public static interface IComplexCustomNodeFactoryParams extends INodeFactoryParams<ComplexCustomNode> {

		String additionalProperty();

	}

	record ComplexCustomNodeFactoryParams(String id, String additionalProperty)
			implements IComplexCustomNodeFactoryParams {

	}

	@Test
	public void testCreateValidComplexCustomNode() {
		ComplexCustomNode node = new ComplexCustomNodeFactory()
				.createNode(new ComplexCustomNodeFactoryParams("nodeIdValue", "additionaPropValue"));
		Assertions.assertNotNull(node);
		Assertions.assertEquals("nodeIdValue", node.getId());
		Assertions.assertEquals("additionaPropValue", node.getAdditionalProperty());
		INode nodeWithGeneratedName = new ComplexCustomNodeFactory()
				.createNode(new ComplexCustomNodeFactoryParams(null, "additionaPropValue"));
		Assertions.assertNotNull(nodeWithGeneratedName);
		Assertions.assertEquals("additionaPropValue", node.getAdditionalProperty());
	}

}
