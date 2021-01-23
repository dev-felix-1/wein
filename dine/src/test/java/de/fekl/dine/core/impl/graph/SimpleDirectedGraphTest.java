package de.fekl.dine.core.impl.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.impl.edge.SimpleEdge;
import de.fekl.dine.core.impl.node.SimpleNode;

public class SimpleDirectedGraphTest {

	@Test
	public void testCreateDirectedGraph() {
		Assertions.assertDoesNotThrow(
				() -> new SimpleDirectedGraph<INode>(Collections.singleton(new SimpleNode("A")), new ArrayList<>()));
		synchronized (SimpleDirectedGraph.class) {
			SimpleDirectedGraph.ALLOW_EMPTY_NODE_SET = true;
			Assertions.assertDoesNotThrow(
					() -> new SimpleDirectedGraph<INode>(Collections.emptySet(), new ArrayList<>()));
			SimpleDirectedGraph.ALLOW_EMPTY_NODE_SET = false;
		}
		
		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		SimpleDirectedGraph<INode> simpleDirectedGraph = new SimpleDirectedGraph<INode>(nodes,
				Arrays.asList(new SimpleEdge("A", "B")));
		Assertions.assertTrue(simpleDirectedGraph.contains("A"));
		Assertions.assertTrue(simpleDirectedGraph.contains("B"));
		Assertions.assertFalse(simpleDirectedGraph.contains("C"));

	}

	@Test
	public void testFailOnEmptyNodes() {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new SimpleDirectedGraph<INode>(Collections.emptySet(), new ArrayList<>()));
	}

	@Test
	public void testFailOnInvalidEdges() {
		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));

		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new SimpleDirectedGraph<INode>(nodes, Arrays.asList(new SimpleEdge("A", "C"))));
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new SimpleDirectedGraph<INode>(nodes, Arrays.asList(new SimpleEdge("C", "B"))));
	}

	@Test
	public void testToString() {
		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		SimpleDirectedGraph<INode> simpleDirectedGraph = new SimpleDirectedGraph<INode>(nodes,
				Arrays.asList(new SimpleEdge("A", "B")));
		String string = simpleDirectedGraph.toString();
		Assertions.assertTrue(string.contains("A"));
		Assertions.assertTrue(string.contains("B"));
		Assertions.assertTrue(string.contains("->"));

	}

}
