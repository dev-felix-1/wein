package de.fekl.tone.api.core.x;

import org.junit.Test;

import de.fekl.cone.api.core.SimpleColouredNet;
import de.fekl.cone.api.core.SimpleToken;
import de.fekl.dine.api.core.INet;
import de.fekl.dine.api.core.NodeRoles;
import de.fekl.dine.api.core.SimpleNet;
import de.fekl.dine.api.core.SimpleNode;
import junit.framework.Assert;

public class ConeTest {

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
	public void testAddNodeBehindStartNode() {
		INet simpleNet = createSimpleABCNet();
		SimpleColouredNet simpleColouredNet = new SimpleColouredNet("colouredHello", simpleNet);
		simpleColouredNet.putToken(NID_A, "token1", new SimpleToken());
		simpleColouredNet.putToken(NID_A, "token2", new SimpleToken());
		simpleColouredNet.putToken(NID_B, "token3", new SimpleToken());
		simpleColouredNet.putToken(NID_B, "token4", new SimpleToken());
		simpleColouredNet.removeToken(NID_B, "token4");
		System.err.println(simpleColouredNet.print());
	}


}
