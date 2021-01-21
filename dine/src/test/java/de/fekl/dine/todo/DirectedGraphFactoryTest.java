package de.fekl.dine.todo;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.impl.graph.SimpleDirectedGraph;
import de.fekl.dine.core.impl.graph.SimpleDirectedGraphFactory;
import de.fekl.dine.core.impl.node.SimpleNode;

public class DirectedGraphFactoryTest {

	// DEFAULT IMPL
	@Test
	public void testCreateDirectedGraph() {

		Assertions.assertDoesNotThrow(() -> new SimpleDirectedGraphFactory<>()
				.createDirectedGraph(Collections.singleton(new SimpleNode("A")), new ArrayList<>()));
		synchronized (SimpleDirectedGraph.class) {
			SimpleDirectedGraph.ALLOW_EMPTY_NODE_SET = true;
			Assertions.assertDoesNotThrow(() -> new SimpleDirectedGraphFactory<>()
					.createDirectedGraph(Collections.emptySet(), new ArrayList<>()));
			SimpleDirectedGraph.ALLOW_EMPTY_NODE_SET = false;
		}
	}

	@Test
	public void testFailOnEmptyNodes() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleDirectedGraphFactory<>()
				.createDirectedGraph(Collections.emptySet(), new ArrayList<>()));
	}

	// CUSTOM IMPL

}
