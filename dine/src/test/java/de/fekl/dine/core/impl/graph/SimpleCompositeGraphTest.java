package de.fekl.dine.core.impl.graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.impl.edge.SimpleEdge;
import de.fekl.dine.core.impl.node.SimpleNode;

public class SimpleCompositeGraphTest {

	@Test
	public void testGraphComposition() {

		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		SimpleDirectedGraph<INode> graph = new SimpleDirectedGraph<INode>(nodes,
				Arrays.asList(new SimpleEdge("A", "B")));

		Set<INode> nodes2 = new HashSet<>();
		nodes2.add(new SimpleNode("C"));
		nodes2.add(new SimpleNode("D"));
		SimpleDirectedGraph<INode> graph2 = new SimpleDirectedGraph<INode>(nodes2,
				Arrays.asList(new SimpleEdge("C", "D")));

		SimpleCompositeGraph<SimpleDirectedGraph<INode>, INode> compositeGraph = new SimpleCompositeGraph<>(
				Arrays.asList(graph, graph2), null);

	}

}
