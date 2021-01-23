package de.fekl.dine.core.impl.sponge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.impl.edge.SimpleEdge;
import de.fekl.dine.core.impl.graph.SimpleDirectedGraph;
import de.fekl.dine.core.impl.node.SimpleNode;

public class SimpleSpongeNetTest {

	@Test
	public void testCalculatePaths() {

		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		nodes.add(new SimpleNode("C"));
		nodes.add(new SimpleNode("D"));
		nodes.add(new SimpleNode("E"));
		nodes.add(new SimpleNode("F"));
		nodes.add(new SimpleNode("G"));
		nodes.add(new SimpleNode("H"));
		List<IEdge> edges = new ArrayList<>();
		edges.add(new SimpleEdge("A", "B"));
		edges.add(new SimpleEdge("A", "C"));
		edges.add(new SimpleEdge("B", "D"));
		edges.add(new SimpleEdge("C", "D"));
		edges.add(new SimpleEdge("D", "E"));
		edges.add(new SimpleEdge("E", "F"));
		edges.add(new SimpleEdge("F", "G"));
		edges.add(new SimpleEdge("F", "H"));
		edges.add(new SimpleEdge("G", "H"));

		SimpleDirectedGraph<INode> graph = new SimpleDirectedGraph<>(nodes, edges);
		SimpleSpongeNet<INode> spongeNet = new SimpleSpongeNet<>(graph, "A");

		List<List<IEdge>> paths = spongeNet.getPaths("A", "E");
		Assertions.assertEquals(2, paths.size());

		Assertions.assertEquals("A", paths.get(0).get(0).getSource());
		Assertions.assertEquals("B", paths.get(0).get(0).getTarget());
		Assertions.assertEquals("B", paths.get(0).get(1).getSource());
		Assertions.assertEquals("D", paths.get(0).get(1).getTarget());
		Assertions.assertEquals("D", paths.get(0).get(2).getSource());
		Assertions.assertEquals("E", paths.get(0).get(2).getTarget());

		Assertions.assertEquals("A", paths.get(1).get(0).getSource());
		Assertions.assertEquals("C", paths.get(1).get(0).getTarget());

		paths = spongeNet.getPaths("A", "H");
		Assertions.assertEquals(4, paths.size());

		Assertions.assertEquals("A", paths.get(0).get(0).getSource());
		Assertions.assertEquals("B", paths.get(0).get(0).getTarget());
		Assertions.assertEquals("B", paths.get(0).get(1).getSource());
		Assertions.assertEquals("D", paths.get(0).get(1).getTarget());
		Assertions.assertEquals("D", paths.get(0).get(2).getSource());
		Assertions.assertEquals("E", paths.get(0).get(2).getTarget());

		Assertions.assertEquals("A", paths.get(1).get(0).getSource());
		Assertions.assertEquals("C", paths.get(1).get(0).getTarget());
	}

	@Test
	public void testToString() {

		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		nodes.add(new SimpleNode("C"));
		List<IEdge> edges = new ArrayList<>();
		edges.add(new SimpleEdge("A", "B"));
		edges.add(new SimpleEdge("B", "C"));

		SimpleDirectedGraph<INode> graph = new SimpleDirectedGraph<>(nodes, edges);
		SimpleSpongeNet<INode> simpleSpongeNet = new SimpleSpongeNet<>(graph, "A");

		String string = simpleSpongeNet.toString();
		Assertions.assertTrue(string.contains("A"));
		Assertions.assertTrue(string.contains("B"));
		Assertions.assertTrue(string.contains("C"));
		Assertions.assertTrue(string.contains("->"));

	}

	@Test
	public void testFailOnUnconnected() {

		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		nodes.add(new SimpleNode("C"));
		List<IEdge> edges = new ArrayList<>();
		edges.add(new SimpleEdge("A", "B"));

		SimpleDirectedGraph<INode> graph = new SimpleDirectedGraph<>(nodes, edges);
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleSpongeNet<>(graph, "A"));

	}

	@Test
	public void testFailOnCycle() {

		// easy cycle
		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		nodes.add(new SimpleNode("C"));
		List<IEdge> edges = new ArrayList<>();
		edges.add(new SimpleEdge("A", "B"));
		edges.add(new SimpleEdge("B", "C"));
		edges.add(new SimpleEdge("C", "A"));

		SimpleDirectedGraph<INode> graph = new SimpleDirectedGraph<>(nodes, edges);
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleSpongeNet<>(graph, "A"));

		// more difficult cycle
		nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		nodes.add(new SimpleNode("C"));
		nodes.add(new SimpleNode("D"));
		nodes.add(new SimpleNode("E"));
		nodes.add(new SimpleNode("F"));
		nodes.add(new SimpleNode("G"));
		nodes.add(new SimpleNode("H"));
		edges = new ArrayList<>();
		edges.add(new SimpleEdge("A", "B"));
		edges.add(new SimpleEdge("B", "C"));
		edges.add(new SimpleEdge("C", "D"));
		edges.add(new SimpleEdge("C", "E"));
		edges.add(new SimpleEdge("D", "F"));
		edges.add(new SimpleEdge("E", "F"));
		edges.add(new SimpleEdge("D", "G"));
		edges.add(new SimpleEdge("G", "H"));
		edges.add(new SimpleEdge("G", "C"));

		SimpleDirectedGraph<INode> graph2 = new SimpleDirectedGraph<>(nodes, edges);
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleSpongeNet<>(graph2, "A"));

	}

	@Test
	public void testConstructionCorrect() {

		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		nodes.add(new SimpleNode("C"));
		nodes.add(new SimpleNode("D"));
		nodes.add(new SimpleNode("E"));
		nodes.add(new SimpleNode("F"));
		nodes.add(new SimpleNode("G"));
		nodes.add(new SimpleNode("H"));
		List<IEdge> edges = new ArrayList<>();
		edges.add(new SimpleEdge("A", "B"));
		edges.add(new SimpleEdge("A", "C"));
		edges.add(new SimpleEdge("B", "D"));
		edges.add(new SimpleEdge("C", "D"));
		edges.add(new SimpleEdge("D", "E"));
		edges.add(new SimpleEdge("E", "F"));
		edges.add(new SimpleEdge("F", "G"));
		edges.add(new SimpleEdge("F", "H"));

		SimpleDirectedGraph<INode> graph = new SimpleDirectedGraph<>(nodes, edges);
		SimpleSpongeNet<INode> spongeNet = new SimpleSpongeNet<>(graph, "A");

		Assertions.assertEquals(8, spongeNet.getEdges().size());
		Assertions.assertEquals(8, spongeNet.getNodes().size());
		Assertions.assertEquals(8, spongeNet.getNodeIds().size());
		Assertions.assertTrue(spongeNet.getNodeIds().contains("A"));
		Assertions.assertTrue(spongeNet.getNodeIds().contains("B"));
		Assertions.assertTrue(spongeNet.getNodeIds().contains("C"));
		Assertions.assertTrue(spongeNet.getNodeIds().contains("D"));
		Assertions.assertTrue(spongeNet.getNodeIds().contains("E"));
		Assertions.assertTrue(spongeNet.getNodeIds().contains("F"));
		Assertions.assertTrue(spongeNet.getNodeIds().contains("G"));
		Assertions.assertTrue(spongeNet.getNodeIds().contains("H"));
		Assertions.assertEquals(2, spongeNet.getLeafs().size());
		Assertions.assertEquals(2, spongeNet.getLeafIds().size());
		Assertions.assertTrue(spongeNet.isLeaf("H"));
		Assertions.assertTrue(spongeNet.isLeaf("G"));
		Assertions.assertFalse(spongeNet.isLeaf("F"));
		Assertions.assertFalse(spongeNet.isLeaf(spongeNet.getNode("F")));
		Assertions.assertEquals(2, spongeNet.getLeafIds().size());
		Assertions.assertTrue(spongeNet.getLeafIds().contains("H"));
		Assertions.assertTrue(spongeNet.getLeafIds().contains("G"));
		Assertions.assertEquals("A", spongeNet.getRootId());
		Assertions.assertEquals("A", spongeNet.getRoot().getId());
		Assertions.assertTrue(spongeNet.isRoot("A"));
		Assertions.assertFalse(spongeNet.isRoot("B"));
		Assertions.assertTrue(spongeNet.isRoot(spongeNet.getNode("A")));

	}

	@Test
	public void testStartnodeNotInGraph() {

		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		List<IEdge> edges = new ArrayList<>();
		edges.add(new SimpleEdge("A", "B"));

		SimpleDirectedGraph<INode> graph = new SimpleDirectedGraph<>(nodes, edges);
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleSpongeNet<INode>(graph, "C"));

	}

}
