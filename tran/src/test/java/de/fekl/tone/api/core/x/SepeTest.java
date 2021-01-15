package de.fekl.tone.api.core.x;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.graph.DirectedGraphBuilder;
import de.fekl.dine.api.node.SimpleNode;
import de.fekl.dine.api.node.SimpleNodeBuilder;
import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.state.SimpleToken;
import de.fekl.dine.api.state.SimpleTokenFactory;
import de.fekl.dine.api.state.SimpleTokenStore;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.dine.api.tree.SpongeNetBuilder;
import de.fekl.esta.api.core.IStateChangeOperation;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.sepe.ColouredNetOperations;
import de.fekl.sepe.ColouredNetProcessingContainer;

public class SepeTest {

	private static final String NID_A = "A";
	private static final String NID_B = "B";
	private static final String NID_B2 = "B2";
	private static final String NID_C = "C";
	private static final String NID_D = "D";

	private static ISpongeNet<SimpleNode> createSimpleABCNet() {
		return
		//@formatter:off
			new SpongeNetBuilder<SimpleNode>()
				.setGraph(new DirectedGraphBuilder<SimpleNode>()
					.setNodeBuilder(new SimpleNodeBuilder())
					.addNode(NID_A)
					.addNode(NID_B)
					.addNode(NID_B2)
					.addNode(NID_C)
					.addNode(NID_D)
					.addEdge(NID_A, NID_B)
					.addEdge(NID_A, NID_B2)
					.addEdge(NID_B, NID_C)
					.addEdge(NID_B, NID_D)
					.build())
				.setStartNode(NID_A)
				.build();
		//@formatter:on
	}

	private static ISpongeNet<SimpleNode> createSimpleSplitterABCNet() {
		return
		//@formatter:off
			new SpongeNetBuilder<SimpleNode>()
				.setGraph(new DirectedGraphBuilder<SimpleNode>()
					.setNodeBuilder(new SimpleNodeBuilder())
					.addNode(new TokenCopyNode(NID_A))
					.addNode(NID_B)
					.addNode(NID_B2)
					.addNode(NID_C)
					.addNode(NID_D)
					.addEdge(NID_A, NID_B)
					.addEdge(NID_A, NID_B2)
					.addEdge(NID_B, NID_C)
					.addEdge(NID_B, NID_D)					
					.build())
				.setStartNode(NID_A)
				.build();
		//@formatter:on
	}

	@Test
	public void testProcessing() {
		ISpongeNet<SimpleNode> simpleNet = createSimpleABCNet();

		ColouredNetProcessingContainer<SimpleNode, SimpleToken> colouredNetProcessingContainer = new ColouredNetProcessingContainer<>(
				simpleNet, new SimpleTokenStore<>(), new SimpleTokenFactory());

		colouredNetProcessingContainer.process(new SimpleToken("hi"));

	}

	@Test
	public void testSplit() {
		ISpongeNet<SimpleNode> simpleNet = createSimpleSplitterABCNet();

		ColouredNetProcessingContainer<SimpleNode, IToken> colouredNetProcessingContainer = new ColouredNetProcessingContainer<>(
				simpleNet, new SimpleTokenStore<>(), null) {

			protected <C extends IStateContainer<ITokenStore<IToken>>> void handleToken(C currentState, String tokenId,
					String nodeId, boolean split) {

				List<IEdge> outgoingEdges = getNet().getOutgoingEdges(nodeId);

				if (getNet().getNode(nodeId) instanceof TokenCopyNode) {

					int size = outgoingEdges.size();
					if (size > 1) {
						IStateChangeOperation<ITokenStore<SimpleToken>> copyTokenOperation = ColouredNetOperations
								.copyToken(tokenId, size - 1, new SimpleTokenFactory());
						System.err.println(copyTokenOperation);
						currentState.changeState((IStateChangeOperation<ITokenStore<IToken>>)(IStateChangeOperation<?>)copyTokenOperation);
					}

					List<IToken> tokensOnNode = new ArrayList<>(currentState.getCurrentState().getTokens(nodeId));
					Assertions.assertEquals(tokensOnNode.size(), outgoingEdges.size());
					IntStream.range(0, tokensOnNode.size()).forEach(i -> {
						String tId = tokensOnNode.get(i).getId();
						String nId = outgoingEdges.get(i).getTarget();
						IStateChangeOperation<ITokenStore<IToken>> moveTokenOperation = ColouredNetOperations
								.moveToken(nodeId, nId, tId);
						System.err.println(moveTokenOperation);
						currentState.changeState(moveTokenOperation);
					});

				} else {
					if (!outgoingEdges.isEmpty()) {
						IEdge iEdge = outgoingEdges.get(0);
						String target = iEdge.getTarget();
						IStateChangeOperation<ITokenStore<IToken>> moveTokenOperation = ColouredNetOperations
								.moveToken(nodeId, target, tokenId);
						System.err.println(moveTokenOperation);
						currentState.changeState(moveTokenOperation);
					}
				}

			}
		};

		colouredNetProcessingContainer.process(new SimpleToken("helou"));

	}

}
