package de.fekl.dine.todo;

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
import de.fekl.dine.core.impl.sponge.SimpleSpongeNet;

public class SpongeNetPathsTest {
	
	@Test
	public void test() {
		Set<INode> nodes = new HashSet<>();
		nodes.add(new SimpleNode("A"));
		nodes.add(new SimpleNode("B"));
		nodes.add(new SimpleNode("C"));
		nodes.add(new SimpleNode("D"));
		nodes.add(new SimpleNode("E"));
		nodes.add(new SimpleNode("F"));
		List<IEdge> edges = new ArrayList<>();
		edges.add(new SimpleEdge("A", "B"));
		edges.add(new SimpleEdge("A", "C"));
		edges.add(new SimpleEdge("B", "D"));
		edges.add(new SimpleEdge("C", "D"));
		edges.add(new SimpleEdge("D", "E"));
		edges.add(new SimpleEdge("E", "F"));
		
		SimpleDirectedGraph<INode> graph = new SimpleDirectedGraph<>(nodes, edges);
		SimpleSpongeNet<INode> spongeNet = new SimpleSpongeNet<>(graph, "A");
		
		List<List<IEdge>> paths = spongeNet.getPaths("A", "E");
		Assertions.assertEquals(2, paths.size());
		
		Assertions.assertEquals("A", paths.get(0).get(0).getSource());
		Assertions.assertEquals("B", paths.get(0).get(0).getTarget());
		
		Assertions.assertEquals("A", paths.get(1).get(0).getSource());
		Assertions.assertEquals("C", paths.get(1).get(0).getTarget());
		
	}

}
