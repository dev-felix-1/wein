package de.fekl.dine;

import org.junit.jupiter.api.Test;

import de.fekl.dine.api.graph.DirectedGraphBuilder;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.SimpleNode;

public class DirectedGraphBuilderTest {

	// DEFAULT IMPL
	@Test
	public void testCreateDirectedGraph() {

		DirectedGraphBuilder directedGraphBuilder = new DirectedGraphBuilder();

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

	@Test
	public void testCreateCustomDirectedGraph() {
		// formatter:off
		new DirectedGraphBuilder<>().addNode(new ComplexCustomNode("hello", "goodby"));

	}

}
