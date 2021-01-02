package de.fekl.tone.api.core.x;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import de.fekl.cone.api.core.IColouredNet;
import de.fekl.cone.api.core.IToken;
import de.fekl.cone.api.core.SimpleColouredNet;
import de.fekl.cone.api.core.SimpleToken;
import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.INet;
import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.core.NodeRoles;
import de.fekl.dine.api.core.SimpleNet;
import de.fekl.dine.api.core.SimpleNode;
import de.fekl.esta.api.core.IStateChangeOperation;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.sepe.ColouredNetOperations;
import de.fekl.sepe.ColouredNetProcessingContainer;
import de.fekl.tone.api.core.x.claz.ITransformer;
import de.fekl.tone.api.core.x.claz.Message;
import de.fekl.tone.api.core.x.claz.RequestTransformer;
import de.fekl.tone.api.core.x.claz.ResponseTransformer;
import de.fekl.tone.api.core.x.claz.TokenCopyNode;
import junit.framework.Assert;

public class SepeTest {

	private static final String NID_A = "A";
	private static final String NID_B = "B";
	private static final String NID_B2 = "B2";
	private static final String NID_C = "C";
	private static final String NID_D = "D";

	private static INet createSimpleABCNet() {
		INet simpleNet = new SimpleNet("hello");
		simpleNet.addNode(NID_A, NodeRoles.START, new SimpleNode());
		simpleNet.addNode(NID_B, NodeRoles.INTERMEDIATE, new SimpleNode());
		simpleNet.addNode(NID_B2, NodeRoles.INTERMEDIATE, new SimpleNode());
		simpleNet.addNode(NID_C, NodeRoles.END, new SimpleNode());
		simpleNet.addNode(NID_D, NodeRoles.END, new SimpleNode());
		simpleNet.addEdge(NID_A, NID_B);
		simpleNet.addEdge(NID_A, NID_B2);
		simpleNet.addEdge(NID_B, NID_C);
		simpleNet.addEdge(NID_B, NID_D);
		return simpleNet;
	}

	private static INet createSimpleSplitterABCNet() {
		INet simpleNet = new SimpleNet("hello");
		simpleNet.addNode(NID_A, NodeRoles.START, new TokenCopyNode());
		simpleNet.addNode(NID_B, NodeRoles.INTERMEDIATE, new SimpleNode());
		simpleNet.addNode(NID_B2, NodeRoles.INTERMEDIATE, new SimpleNode());
		simpleNet.addNode(NID_C, NodeRoles.END, new SimpleNode());
		simpleNet.addNode(NID_D, NodeRoles.END, new SimpleNode());
		simpleNet.addEdge(NID_A, NID_B);
		simpleNet.addEdge(NID_A, NID_B2);
		simpleNet.addEdge(NID_B, NID_C);
		simpleNet.addEdge(NID_B, NID_D);
		return simpleNet;
	}
	
	private static INet createSimpleTransformerABCNet() {
		INet simpleNet = new SimpleNet("hello");
		simpleNet.addNode(NID_A, NodeRoles.START, new RequestTransformer());
		simpleNet.addNode(NID_B, NodeRoles.INTERMEDIATE, new ResponseTransformer());
		simpleNet.addNode(NID_C, NodeRoles.END, new SimpleNode());
		simpleNet.addEdge(NID_A, NID_B);
		simpleNet.addEdge(NID_B, NID_C);
		return simpleNet;
	}

	@Test
	public void testProcessing() {
		INet simpleNet = createSimpleABCNet();
		SimpleColouredNet simpleColouredNet = new SimpleColouredNet("colouredHello", simpleNet);

		ColouredNetProcessingContainer colouredNetProcessingContainer = new ColouredNetProcessingContainer(
				simpleColouredNet);

		colouredNetProcessingContainer.process(new SimpleToken());

	}

	@Test
	public void testSplit() {
		INet simpleNet = createSimpleSplitterABCNet();
		SimpleColouredNet simpleColouredNet = new SimpleColouredNet("colouredHello", simpleNet);

		ColouredNetProcessingContainer colouredNetProcessingContainer = new ColouredNetProcessingContainer(
				simpleColouredNet) {

			protected void handleToken(IStateContainer<IColouredNet> currentState, String tokenId, String nodeId) {

				List<IEdge> outgoingEdges = currentState.getCurrentState().getNet().getOutgoingEdges(nodeId);

				if (currentState.getCurrentState().getNet().getNode(nodeId) instanceof TokenCopyNode) {

					int size = outgoingEdges.size();
					if (size > 1) {
						IStateChangeOperation<IColouredNet> copyTokenOperation = ColouredNetOperations
								.copyToken(tokenId, size - 1);
						System.err.println(copyTokenOperation);
						currentState.changeState(copyTokenOperation);
					}
					
					List<String> tokensOnNode = currentState.getCurrentState().getTokensOnNode(nodeId);
					Assert.assertEquals(tokensOnNode.size(), outgoingEdges.size());
					IntStream.range(0, tokensOnNode.size()).forEach(i->{
						String tId = tokensOnNode.get(i);
						String nId = outgoingEdges.get(i).getTarget();
						IStateChangeOperation<IColouredNet> moveTokenOperation = ColouredNetOperations.moveToken(nodeId,
								nId, tId);
						System.err.println(moveTokenOperation);
						currentState.changeState(moveTokenOperation);
					});

				} else {
					if (!outgoingEdges.isEmpty()) {
						IEdge iEdge = outgoingEdges.get(0);
						String target = iEdge.getTarget();
						IStateChangeOperation<IColouredNet> moveTokenOperation = ColouredNetOperations.moveToken(nodeId,
								target, tokenId);
						System.err.println(moveTokenOperation);
						currentState.changeState(moveTokenOperation);
					}
				}

			}
		};

		colouredNetProcessingContainer.process(new SimpleToken());

	}
	
	@Test
	public void testTransformerNet() {
		INet simpleNet = createSimpleTransformerABCNet();
		SimpleColouredNet simpleColouredNet = new SimpleColouredNet("colouredHello", simpleNet);

		ColouredNetProcessingContainer colouredNetProcessingContainer = new ColouredNetProcessingContainer(
				simpleColouredNet) {

			protected void handleToken(IStateContainer<IColouredNet> currentState, String tokenId, String nodeId) {

				IToken token = currentState.getCurrentState().getToken(tokenId);
				if (token instanceof Message) {

					INode node = currentState.getCurrentState().getNet().getNode(nodeId);
					if (node instanceof ITransformer) {
						((ITransformer) node).transform((Message) token);
					}
					System.err.println(token);
				}

				super.handleToken(currentState, tokenId, nodeId);

			}
		};

		colouredNetProcessingContainer.process(new Message());

	}

}
