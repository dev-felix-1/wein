package de.fekl.dine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.api.edge.conditional.ICondition;
import de.fekl.dine.api.edge.conditional.SimpleConditionalEdge;
import de.fekl.dine.api.edge.conditional.SimpleConditionalEdgeBuilder;
import de.fekl.dine.api.graph.DirectedGraphBuilder;
import de.fekl.dine.api.graph.IDirectedGraph;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.SimpleNode;

public class ConditionalEdgeTest {

	@Test
	public void testGraphFactoryWithConditionalEdges() {
		IDirectedGraph<INode> graph =
		//@formatter:off
		new DirectedGraphBuilder<>()
			.addNode(new SimpleNode("A"))
			.addNode(new SimpleNode("B"))
			.addNode(new SimpleNode("C"))
			.addEdge(new SimpleConditionalEdge("A", "B", new ICondition() {}))
			.addEdge(new SimpleConditionalEdge("B", "C", new ICondition() {}))
			.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getEdges().size());
	}

	@Test
	public void testGraphFactoryWithConditionalEdgesBuilderSet() {
		IDirectedGraph<INode> graph =
		//@formatter:off
		new DirectedGraphBuilder<>()
			.addNode(new SimpleNode("A"))
			.addNode(new SimpleNode("B"))
			.addNode(new SimpleNode("C"))
			.addEdge("A", "B")
			.addEdge("B", "C")
			.setEdgeBuilder(new SimpleConditionalEdgeBuilder())
			.build();
		//@formatter:on
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getEdges().size());
		Assertions.assertEquals(SimpleConditionalEdge.class, graph.getEdges().get(0).getClass());
	}

}
