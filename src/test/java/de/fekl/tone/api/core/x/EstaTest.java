package de.fekl.tone.api.core.x;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.cone.api.core.SimpleColouredNet;
import de.fekl.cone.api.core.SimpleToken;
import de.fekl.dine.api.core.INet;
import de.fekl.dine.api.core.NodeRoles;
import de.fekl.dine.api.core.SimpleNet;
import de.fekl.dine.api.core.SimpleNodeDeprecated;
import de.fekl.esta.api.core.IStateHasChangedEvent;
import de.fekl.esta.api.core.SimpleStateContainer;

public class EstaTest {

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

	@Test
	public void testStateContainerWithNet() {
		INet simpleNet = createSimpleABCNet();
		SimpleColouredNet simpleColouredNet = new SimpleColouredNet("colouredHello", simpleNet);
		System.err.println(simpleColouredNet.print());
		
		SimpleStateContainer<SimpleColouredNet> simpleStateContainer = new SimpleStateContainer<>(simpleColouredNet);
		simpleStateContainer.changeState(current -> {
			SimpleColouredNet newState = new SimpleColouredNet(current);
			newState.putToken(NID_A, "token1", new SimpleToken());
			return newState;
		});
		
		IStateHasChangedEvent<SimpleColouredNet> poll = simpleStateContainer.getStateChangedEvents().poll();
		
		System.err.println(simpleStateContainer.getCurrentState().print());
		System.err.println(poll.getSourceState().print());
		System.err.println(poll.getTargetState().print());
		
		simpleColouredNet.putToken(NID_A, "token1", new SimpleToken());
		simpleColouredNet.putToken(NID_A, "token2", new SimpleToken());
		simpleColouredNet.putToken(NID_B, "token3", new SimpleToken());
		simpleColouredNet.putToken(NID_B, "token4", new SimpleToken());
		simpleColouredNet.removeToken(NID_B, "token4");

	}

	@Test
	public void testStateContainer() {
		SimpleStateContainer<String> simpleStateContainer = new SimpleStateContainer<>("init");
		Assertions.assertEquals(0, simpleStateContainer.getStateChangedEvents().size());
		simpleStateContainer.changeState(initial -> initial + "-changed");
		Assertions.assertEquals(1, simpleStateContainer.getStateChangedEvents().size());
		IStateHasChangedEvent<String> poll = simpleStateContainer.getStateChangedEvents().poll();
		Assertions.assertEquals("init", poll.getSourceState());
		Assertions.assertEquals("init-changed", poll.getTargetState());
		
	}

}
