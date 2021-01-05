package de.fekl.tone.api.core.x;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.api.core.INet;
import de.fekl.dine.api.core.NodeRoles;
import de.fekl.dine.api.core.SimpleNet;
import de.fekl.dine.api.core.SimpleNodeDeprecated;
import de.fekl.dine.api.graph.DirectedGraphBuilder;
import de.fekl.dine.api.graph.IDirectedGraph;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.dine.api.tree.SpongeNetBuilder;

public class DineTest {

	private static final String NID_A = "A";
	private static final String NID_B = "B";
	private static final String NID_C = "C";
	private static final String NID_D = "D";

	private static INet createSimpleABCNet() {
		INet simpleNet = new SimpleNet("hello");
		simpleNet.addNode(NID_A, NodeRoles.START, new SimpleNodeDeprecated());
		simpleNet.addNode(NID_B, NodeRoles.INTERMEDIATE, new SimpleNodeDeprecated());
		simpleNet.addNode(NID_C, NodeRoles.END, new SimpleNodeDeprecated());
		simpleNet.addEdge(NID_A, NID_B);
		simpleNet.addEdge(NID_B, NID_C);
		return simpleNet;
	}

	private static IDirectedGraph createSimpleABCGraph() {
		return new DirectedGraphBuilder().addNode(NID_A).addNode(NID_B).addNode(NID_C).addEdge(NID_A, NID_B)
				.addEdge(NID_B, NID_C).build();
	}

	@Test
	public void testGraph() {
		IDirectedGraph graph = createSimpleABCGraph();

		Assertions.assertFalse(graph.contains("X"));

		Assertions.assertTrue(graph.contains(NID_A));
		Assertions.assertTrue(graph.contains(NID_B));
		Assertions.assertTrue(graph.contains(NID_C));

		Assertions.assertEquals(2, graph.getEdges().size());

		Assertions.assertEquals(0, graph.getIncomingEdges(NID_A).size());
		Assertions.assertEquals(1, graph.getOutgoingEdges(NID_A).size());

		Assertions.assertEquals(1, graph.getIncomingEdges(NID_B).size());
		Assertions.assertEquals(1, graph.getOutgoingEdges(NID_B).size());

		Assertions.assertEquals(1, graph.getIncomingEdges(NID_C).size());
		Assertions.assertEquals(0, graph.getOutgoingEdges(NID_C).size());

		System.err.println(graph);
	}

	@Test
	public void testSpongeNet() {
		ISpongeNet spongeNet =
		//@formatter:off
			new SpongeNetBuilder()
				.setGraph(new DirectedGraphBuilder()
					.addNode(NID_A)
					.addNode(NID_B)
					.addNode(NID_C)
					.addEdge(NID_A, NID_B)
					.addEdge(NID_B, NID_C)
					.build())
				.setStartNode(NID_A)
				.build();
		//@formatter:on

		Assertions.assertFalse(spongeNet.contains("X"));

		Assertions.assertTrue(spongeNet.contains(NID_A));
		Assertions.assertTrue(spongeNet.contains(NID_B));
		Assertions.assertTrue(spongeNet.contains(NID_C));

		Assertions.assertEquals(2, spongeNet.getEdges().size());

		Assertions.assertEquals(0, spongeNet.getIncomingEdges(NID_A).size());
		Assertions.assertEquals(1, spongeNet.getOutgoingEdges(NID_A).size());

		Assertions.assertEquals(1, spongeNet.getIncomingEdges(NID_B).size());
		Assertions.assertEquals(1, spongeNet.getOutgoingEdges(NID_B).size());

		Assertions.assertEquals(1, spongeNet.getIncomingEdges(NID_C).size());
		Assertions.assertEquals(0, spongeNet.getOutgoingEdges(NID_C).size());

		Assertions.assertEquals(1, spongeNet.getLeafs().size());
		Assertions.assertEquals(NID_A, spongeNet.getRoot().getId());

		System.err.println(spongeNet);
	}

	@Test
	public void testSpongeNetFailOnCycle() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		//@formatter:off
			new SpongeNetBuilder()
				.setGraph(new DirectedGraphBuilder()
					.addNode(NID_A)
					.addNode(NID_B)
					.addNode(NID_C)
					.addEdge(NID_A, NID_B)
					.addEdge(NID_B, NID_C)
					.addEdge(NID_C, NID_A)
					.build())
				.setStartNode(NID_A)
				.build()
		//@formatter:on
		);
	}

	@Test
	public void testSpongeNetFailOnDisconnected() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		//@formatter:off
			new SpongeNetBuilder()
				.setGraph(new DirectedGraphBuilder()
					.addNode(NID_A)
					.addNode(NID_B)
					.addNode(NID_C)
					.addEdge(NID_A, NID_B)
					.build())
			.setStartNode(NID_A)
			.build()
		//@formatter:on
		);
	}

	@Test
	public void testConvenienceNotations1() {
		INet simpleNet = new SimpleNet("hello");
		simpleNet.addNode(NID_A, NodeRoles.START);
		simpleNet.addNode(NID_B, NodeRoles.INTERMEDIATE);
		simpleNet.addNode(NID_C);
		simpleNet.addNode(NID_D, NodeRoles.END);
		simpleNet.addEdge(NID_A, NID_B);
		simpleNet.addEdge(NID_B, NID_C);
		simpleNet.addEdge(NID_C, NID_D);

		Assertions.assertNull(simpleNet.getNode("X"));
		Assertions.assertNotNull(simpleNet.getNode(NID_A));
		Assertions.assertNotNull(simpleNet.getNode(NID_B));
		Assertions.assertNotNull(simpleNet.getNode(NID_C));
		Assertions.assertNotNull(simpleNet.getNode(NID_D));
	}

	@Test
	public void testAddNodeBehindStartNode() {
		INet simpleNet = createSimpleABCNet();
		simpleNet.addNode(NID_D, NodeRoles.INTERMEDIATE, new SimpleNodeDeprecated());
		simpleNet.addEdge(NID_A, NID_D);
		System.err.println(simpleNet.print());
	}

	@Test
	public void test1() {
		INet simpleNet = createSimpleABCNet();
		System.err.println(simpleNet.print());
	}

	// ERROR Tests

	@Test
	public void testErrorOnDuplicateNodeDefinition() {
		Assertions.assertThrows(IllegalStateException.class,
				() -> createSimpleABCNet().addNode(NID_B, NodeRoles.INTERMEDIATE, new SimpleNodeDeprecated()));
	}

	@Test
	public void testErrorOnConnectionToMissingNode() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> createSimpleABCNet().addEdge(NID_A, NID_D));
	}

	@Test
	public void testErrorOnDuplicateConnection() {
		Assertions.assertThrows(IllegalStateException.class, () -> createSimpleABCNet().addEdge(NID_A, NID_B));
	}

	@Test
	public void testErrorOnConnectionBeforeStartNode() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			INet simpleNet = createSimpleABCNet();
			simpleNet.addNode(NID_D, NodeRoles.INTERMEDIATE, new SimpleNodeDeprecated());
			simpleNet.addEdge(NID_D, NID_A);
		});
	}

	@Test
	public void testErrorOnConnectionBehindEndNode() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			INet simpleNet = createSimpleABCNet();
			simpleNet.addNode(NID_D, NodeRoles.INTERMEDIATE, new SimpleNodeDeprecated());
			simpleNet.addEdge(NID_C, NID_D);
		});
	}

}
