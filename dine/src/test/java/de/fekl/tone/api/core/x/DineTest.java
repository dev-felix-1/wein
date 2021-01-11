//package de.fekl.tone.api.core.x;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import de.fekl.dine.api.graph.DirectedGraphBuilder;
//import de.fekl.dine.api.graph.IDirectedGraph;
//import de.fekl.dine.api.tree.ISpongeNet;
//import de.fekl.dine.api.tree.SpongeNetBuilder;
//
//public class DineTest {
//
//	private static final String NID_A = "A";
//	private static final String NID_B = "B";
//	private static final String NID_C = "C";
//	private static final String NID_D = "D";
//
//	private static IDirectedGraph createSimpleABCGraph() {
//		return new DirectedGraphBuilder().addNode(NID_A).addNode(NID_B).addNode(NID_C).addEdge(NID_A, NID_B)
//				.addEdge(NID_B, NID_C).build();
//	}
//
//	@Test
//	public void testGraph() {
//		IDirectedGraph graph = createSimpleABCGraph();
//
//		Assertions.assertFalse(graph.contains("X"));
//
//		Assertions.assertTrue(graph.contains(NID_A));
//		Assertions.assertTrue(graph.contains(NID_B));
//		Assertions.assertTrue(graph.contains(NID_C));
//
//		Assertions.assertEquals(2, graph.getEdges().size());
//
//		Assertions.assertEquals(0, graph.getIncomingEdges(NID_A).size());
//		Assertions.assertEquals(1, graph.getOutgoingEdges(NID_A).size());
//
//		Assertions.assertEquals(1, graph.getIncomingEdges(NID_B).size());
//		Assertions.assertEquals(1, graph.getOutgoingEdges(NID_B).size());
//
//		Assertions.assertEquals(1, graph.getIncomingEdges(NID_C).size());
//		Assertions.assertEquals(0, graph.getOutgoingEdges(NID_C).size());
//
//		System.err.println(graph);
//	}
//
//	@Test
//	public void testSpongeNet() {
//		ISpongeNet spongeNet =
//		//@formatter:off
//			new SpongeNetBuilder()
//				.setGraph(new DirectedGraphBuilder()
//					.addNode(NID_A)
//					.addNode(NID_B)
//					.addNode(NID_C)
//					.addEdge(NID_A, NID_B)
//					.addEdge(NID_B, NID_C)
//					.build())
//				.setStartNode(NID_A)
//				.build();
//		//@formatter:on
//
//		Assertions.assertFalse(spongeNet.contains("X"));
//
//		Assertions.assertTrue(spongeNet.contains(NID_A));
//		Assertions.assertTrue(spongeNet.contains(NID_B));
//		Assertions.assertTrue(spongeNet.contains(NID_C));
//
//		Assertions.assertEquals(2, spongeNet.getEdges().size());
//
//		Assertions.assertEquals(0, spongeNet.getIncomingEdges(NID_A).size());
//		Assertions.assertEquals(1, spongeNet.getOutgoingEdges(NID_A).size());
//
//		Assertions.assertEquals(1, spongeNet.getIncomingEdges(NID_B).size());
//		Assertions.assertEquals(1, spongeNet.getOutgoingEdges(NID_B).size());
//
//		Assertions.assertEquals(1, spongeNet.getIncomingEdges(NID_C).size());
//		Assertions.assertEquals(0, spongeNet.getOutgoingEdges(NID_C).size());
//
//		Assertions.assertEquals(1, spongeNet.getLeafs().size());
//		Assertions.assertEquals(NID_A, spongeNet.getRoot().getId());
//
//		System.err.println(spongeNet);
//	}
//
//	@Test
//	public void testSpongeNetFailOnCycle() {
//		Assertions.assertThrows(IllegalArgumentException.class, () ->
//		//@formatter:off
//			new SpongeNetBuilder()
//				.setGraph(new DirectedGraphBuilder()
//					.addNode(NID_A)
//					.addNode(NID_B)
//					.addNode(NID_C)
//					.addEdge(NID_A, NID_B)
//					.addEdge(NID_B, NID_C)
//					.addEdge(NID_C, NID_A)
//					.build())
//				.setStartNode(NID_A)
//				.build()
//		//@formatter:on
//		);
//	}
//
//	@Test
//	public void testSpongeNetFailOnDisconnected() {
//		Assertions.assertThrows(IllegalArgumentException.class, () ->
//		//@formatter:off
//			new SpongeNetBuilder()
//				.setGraph(new DirectedGraphBuilder()
//					.addNode(NID_A)
//					.addNode(NID_B)
//					.addNode(NID_C)
//					.addEdge(NID_A, NID_B)
//					.build())
//			.setStartNode(NID_A)
//			.build()
//		//@formatter:on
//		);
//	}
//
//	
//
//}
