package de.fekl.tone.api.core.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.core.SimpleNode;
import de.fekl.dine.api.graph.DirectedGraphBuilder;
import de.fekl.dine.api.graph.INode;
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
import de.fekl.tone.api.core.x.claz.ITransformer;
import de.fekl.tone.api.core.x.claz.Message;
import de.fekl.tone.api.core.x.claz.RequestTransformer;
import de.fekl.tone.api.core.x.claz.ResponseTransformer;
import de.fekl.tone.api.core.x.claz.TokenCopyNode;

public class SepeTest {

	private static final String NID_A = "A";
	private static final String NID_B = "B";
	private static final String NID_B2 = "B2";
	private static final String NID_C = "C";
	private static final String NID_D = "D";

	private static ISpongeNet<INode> createSimpleABCNet() {
		return
		//@formatter:off
			new SpongeNetBuilder<INode>()
				.setGraph(new DirectedGraphBuilder<INode>()
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

	private static ISpongeNet<INode> createSimpleSplitterABCNet() {
		return
		//@formatter:off
			new SpongeNetBuilder<INode>()
				.setGraph(new DirectedGraphBuilder<INode>()
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

	private static ISpongeNet<INode> createSimpleTransformerABCNet() {
		return
		//@formatter:off
			new SpongeNetBuilder<INode>()
				.setGraph(new DirectedGraphBuilder<INode>()
					.addNode(new RequestTransformer(NID_A))
					.addNode(new ResponseTransformer(NID_B))
					.addNode(NID_C)
					.addNode(NID_D)
					.addEdge(NID_A, NID_B)
					.addEdge(NID_B, NID_C)
					.addEdge(NID_B, NID_D)
					.build())
				.setStartNode(NID_A)
				.build();
		//@formatter:on
	}

	@Test
	public void testProcessing() {
		ISpongeNet<INode> simpleNet = createSimpleABCNet();

		ColouredNetProcessingContainer<INode, IToken> colouredNetProcessingContainer = new ColouredNetProcessingContainer<>(
				simpleNet, new SimpleTokenStore<>(), null);

		colouredNetProcessingContainer.process(new SimpleToken("hi"));

	}

	@Test
	public void testSplit() {
		ISpongeNet<INode> simpleNet = createSimpleSplitterABCNet();

		ColouredNetProcessingContainer<INode,IToken> colouredNetProcessingContainer = new ColouredNetProcessingContainer<>(
				simpleNet, new SimpleTokenStore<>(),null) {

			protected <C extends IStateContainer<ITokenStore<IToken>>> void handleToken(C currentState, String tokenId,
					String nodeId) {

				List<IEdge> outgoingEdges = getNet().getOutgoingEdges(nodeId);

				if (getNet().getNode(nodeId) instanceof TokenCopyNode) {

					int size = outgoingEdges.size();
					if (size > 1) {
						IStateChangeOperation<ITokenStore<IToken>> copyTokenOperation = ColouredNetOperations.copyToken(tokenId,
								size - 1, new SimpleTokenFactory());
						System.err.println(copyTokenOperation);
						currentState.changeState(copyTokenOperation);
					}

					List<IToken> tokensOnNode = new ArrayList<>(currentState.getCurrentState().getTokens(nodeId));
					Assertions.assertEquals(tokensOnNode.size(), outgoingEdges.size());
					Iterator<IToken> iterator = tokensOnNode.iterator();
					IntStream.range(0, tokensOnNode.size()).forEach(i -> {
						String tId = tokensOnNode.get(i).getId();
						String nId = outgoingEdges.get(i).getTarget();
						IStateChangeOperation<ITokenStore<IToken>> moveTokenOperation = ColouredNetOperations.moveToken(nodeId,
								nId, tId);
						System.err.println(moveTokenOperation);
						currentState.changeState(moveTokenOperation);
					});

				} else {
					if (!outgoingEdges.isEmpty()) {
						IEdge iEdge = outgoingEdges.get(0);
						String target = iEdge.getTarget();
						IStateChangeOperation<ITokenStore<IToken>> moveTokenOperation = ColouredNetOperations.moveToken(nodeId,
								target, tokenId);
						System.err.println(moveTokenOperation);
						currentState.changeState(moveTokenOperation);
					}
				}

			}
		};

		colouredNetProcessingContainer.process(new SimpleToken("helou"));

	}

	@Test
	public void testTransformerNet() {
		ISpongeNet<INode> simpleNet = createSimpleTransformerABCNet();

		ColouredNetProcessingContainer<INode,IToken> colouredNetProcessingContainer = new ColouredNetProcessingContainer<>(
				simpleNet, new SimpleTokenStore<>(),null) {

			protected <C extends IStateContainer<ITokenStore<IToken>>> void handleToken(C currentState, String tokenId,
					String nodeId) {

				IToken token = currentState.getCurrentState().getToken(tokenId);
				if (token instanceof Message) {

					INode node = getNet().getNode(nodeId);
					if (node instanceof ITransformer) {
						((ITransformer) node).transform((Message) token);
					}
					System.err.println(token);
				}

				super.handleToken(currentState, tokenId, nodeId);

			}
		};

		colouredNetProcessingContainer.process(new Message("halo"));

	}

}
