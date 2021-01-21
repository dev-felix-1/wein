package de.fekl.dine.todo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.node.AbstractNodeBuilder;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.node.INodeFactory;
import de.fekl.dine.core.api.node.NodeNames;
import de.fekl.dine.core.impl.node.SimpleNode;
import de.fekl.dine.core.impl.node.SimpleNodeBuilder;

public class DirectedGraphBuilderTest {

	// DEFAULT IMPL
	@Test
	public void testCreateDirectedGraphWithNodeIds() {

		//@formatter:off
		IDirectedGraph<SimpleNode> graph = new DirectedGraphBuilder<SimpleNode>()
				.addNode("A")
				.addNode("B")
				.addEdge("A","B")
				.setNodeBuilder(new SimpleNodeBuilder())
				.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());
	}

	@Test
	public void testCreateDirectedGraphWithNodeBuilders() {

		//@formatter:off
		IDirectedGraph<SimpleNode> graph = new DirectedGraphBuilder<SimpleNode>()
				.addNode(new SimpleNodeBuilder().id("A"))
				.addNode(new SimpleNodeBuilder().id("B"))
				.addEdge("A","B")
				.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());

	}

	@Test
	public void testCreateDirectedGraphWithNodeInstances() {

		//@formatter:off
		IDirectedGraph<SimpleNode> graph = new DirectedGraphBuilder<SimpleNode>()
				.addNode(new SimpleNode("A"))
				.addNode(new SimpleNode("B"))
				.addEdge("A","B")
				.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());

	}

	// CUSTOM IMPL
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
	public void testCreateDirectedGraphWithNodeIdsForCustomNode() {

		//@formatter:off
		IDirectedGraph<ComplexCustomNode> graph = new DirectedGraphBuilder<ComplexCustomNode>()
				.addNode("A")
				.addNode("B")
				.addEdge("A","B")
				.setNodeBuilder(new ComplexCustomNodeBuilder())
				.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		for (ComplexCustomNode node : graph.getNodes()) {
			Assertions.assertNull(node.getAdditionalProperty());
		}
		Assertions.assertEquals(1, graph.getEdges().size());

		//@formatter:off
		graph = new DirectedGraphBuilder<ComplexCustomNode>()
				.addNode("A")
				.addNode("B")
				.addEdge("A","B")
				.setNodeBuilder(new ComplexCustomNodeBuilder().additionalProperty("hello"))
				.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		for (ComplexCustomNode node : graph.getNodes()) {
			Assertions.assertEquals("hello", node.getAdditionalProperty());
		}
		Assertions.assertEquals(1, graph.getEdges().size());
	}

	@Test
	public void testCreateDirectedGraphWithNodeBuildersForCustomNode() {

		//@formatter:off
		IDirectedGraph<ComplexCustomNode> graph = new DirectedGraphBuilder<ComplexCustomNode>()
				.addNode(new ComplexCustomNodeBuilder().id("A"))
				.addNode(new ComplexCustomNodeBuilder().id("B"))
				.addEdge("A","B")
				.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());

	}

	@Test
	public void testCreateDirectedGraphWithNodeInstancesForCustomNode() {

		//@formatter:off
		IDirectedGraph<ComplexCustomNode> graph = new DirectedGraphBuilder<ComplexCustomNode>()
				.addNode(new ComplexCustomNode("A","valA"))
				.addNode(new ComplexCustomNode("B","valB"))
				.addEdge("A","B")
				.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());

	}

}
