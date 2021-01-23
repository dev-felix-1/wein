package de.fekl.dine.core.api.sponge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.node.INode;

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
}
