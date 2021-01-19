package de.fekl.dine.api.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.edge.SimpleEdge;
import de.fekl.dine.api.graph.SimpleDirectedGraph;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.SimpleNode;

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

}
