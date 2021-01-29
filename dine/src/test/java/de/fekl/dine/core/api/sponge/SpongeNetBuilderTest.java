package de.fekl.dine.core.api.sponge;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.impl.node.SimpleNode;

public class SpongeNetBuilderTest {

	@Test
	public void testEmptyStartNode() {
		ISpongeNet<INode> spongeNet = new SpongeNetBuilder<>(new DirectedGraphBuilder<>().addEdge("A", "B")).build();
		Assertions.assertEquals("A", spongeNet.getRootId());
		spongeNet = new SpongeNetBuilder<>(new DirectedGraphBuilder<>().addEdge("A", "B").addEdge("B", "C")).build();
		Assertions.assertEquals("A", spongeNet.getRootId());
		spongeNet = new SpongeNetBuilder<>(
				new DirectedGraphBuilder<>().addNode("A").addNode("B").addEdge("A", "B").addEdge("B", "C")).build();
		Assertions.assertEquals("A", spongeNet.getRootId());
	}

	@Test
	public void testDelegateMethods() {
		ISpongeNet<INode> spongeNet = new SpongeNetBuilder<>().addEdge("A", "B").build();
		Assertions.assertEquals("A", spongeNet.getRootId());
		spongeNet = new SpongeNetBuilder<>().addEdge("A", "B").addEdge("B", "C").build();
		Assertions.assertEquals("A", spongeNet.getRootId());
		spongeNet = new SpongeNetBuilder<>().addNode("A").addNode("B").addEdge("A", "B").addEdge("B", "C").build();
		Assertions.assertEquals("A", spongeNet.getRootId());
	}

	@Test
	public void testCreateDirectedGraphWithChainMethod() {

		ISpongeNet<SimpleNode> graph = new SpongeNetBuilder<SimpleNode>().chain("A", "B", "C").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(3, graph.getNodes().size());
		Assertions.assertEquals(2, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().addNode("D").chain("A", "B", "C").addEdge("C", "D").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(4, graph.getNodes().size());
		Assertions.assertEquals(3, graph.getEdges().size());

		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new SpongeNetBuilder<SimpleNode>().chain("A").build());
	}

	@Test
	public void testCreateDirectedGraphWithForkMethod() {
		ISpongeNet<SimpleNode> graph;

		graph = new SpongeNetBuilder<SimpleNode>().fork("A", "B").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().fork("A", "B", "C").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(3, graph.getNodes().size());
		Assertions.assertEquals(2, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().addNode("A").fork("A", "B", "C", "D").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(4, graph.getNodes().size());
		Assertions.assertEquals(3, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().fork(new SimpleNode("A"), "B").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().fork(new SimpleNode("A"), "B", "C").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(3, graph.getNodes().size());
		Assertions.assertEquals(2, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().fork(new SimpleNode("A"), "B", "C", "D").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(4, graph.getNodes().size());
		Assertions.assertEquals(3, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().fork(new SimpleNode("A"), new SimpleNode("B")).build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().fork("A", new SimpleNode("B")).build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());
	}

	@Test
	public void testCreateDirectedGraphWithJoinMethod() {
		ISpongeNet<SimpleNode> graph;

		graph = new SpongeNetBuilder<SimpleNode>().join("A").on("B").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(2, graph.getNodes().size());
		Assertions.assertEquals(1, graph.getEdges().size());

		graph = new SpongeNetBuilder<SimpleNode>().addEdge("A", "B").addEdge("A", "C")
				.join("B", "C").on("D").build();
		Assertions.assertNotNull(graph);
		Assertions.assertEquals(4, graph.getNodes().size());
		Assertions.assertEquals(4, graph.getEdges().size());
	}

}
