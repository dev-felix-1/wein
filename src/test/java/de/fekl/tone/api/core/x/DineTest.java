package de.fekl.tone.api.core.x;

import org.junit.Test;

import de.fekl.dine.api.core.INet;
import de.fekl.dine.api.core.NodeRoles;
import de.fekl.dine.api.core.SimpleNet;
import de.fekl.dine.api.core.SimpleNode;
import junit.framework.Assert;

public class DineTest {

	private static final String NID_A = "A";
	private static final String NID_B = "B";
	private static final String NID_C = "C";
	private static final String NID_D = "D";

	private static INet createSimpleABCNet() {
		INet simpleNet = new SimpleNet("hello");
		simpleNet.addNode(NID_A, NodeRoles.START, new SimpleNode());
		simpleNet.addNode(NID_B, NodeRoles.INTERMEDIATE, new SimpleNode());
		simpleNet.addNode(NID_C, NodeRoles.END, new SimpleNode());
		simpleNet.addEdge(NID_A, NID_B);
		simpleNet.addEdge(NID_B, NID_C);
		return simpleNet;
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

		Assert.assertNull(simpleNet.getNode("X"));
		Assert.assertNotNull(simpleNet.getNode(NID_A));
		Assert.assertNotNull(simpleNet.getNode(NID_B));
		Assert.assertNotNull(simpleNet.getNode(NID_C));
		Assert.assertNotNull(simpleNet.getNode(NID_D));
	}

	@Test
	public void testAddNodeBehindStartNode() {
		INet simpleNet = createSimpleABCNet();
		simpleNet.addNode(NID_D, NodeRoles.INTERMEDIATE, new SimpleNode());
		simpleNet.addEdge(NID_A, NID_D);
		System.err.println(simpleNet.print());
	}

	@Test
	public void test1() {
		INet simpleNet = createSimpleABCNet();
		System.err.println(simpleNet.print());
	}

	// ERROR Tests

	@Test(expected = IllegalStateException.class)
	public void testErrorOnDuplicateNodeDefinition() {
		createSimpleABCNet().addNode(NID_B, NodeRoles.INTERMEDIATE, new SimpleNode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testErrorOnConnectionToMissingNode() {
		createSimpleABCNet().addEdge(NID_A, NID_D);
	}

	@Test(expected = IllegalStateException.class)
	public void testErrorOnDuplicateConnection() {
		createSimpleABCNet().addEdge(NID_A, NID_B);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testErrorOnConnectionBeforeStartNode() {
		INet simpleNet = createSimpleABCNet();
		simpleNet.addNode(NID_D, NodeRoles.INTERMEDIATE, new SimpleNode());
		simpleNet.addEdge(NID_D, NID_A);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testErrorOnConnectionBehindEndNode() {
		INet simpleNet = createSimpleABCNet();
		simpleNet.addNode(NID_D, NodeRoles.INTERMEDIATE, new SimpleNode());
		simpleNet.addEdge(NID_C, NID_D);
	}

}
