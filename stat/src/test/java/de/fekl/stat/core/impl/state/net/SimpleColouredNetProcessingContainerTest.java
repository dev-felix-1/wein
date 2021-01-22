package de.fekl.stat.core.impl.state.net;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.api.sponge.SpongeNetBuilder;
import de.fekl.dine.core.impl.node.SimpleNode;
import de.fekl.stat.core.api.events.IEndNodeReachedEvent;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.events.IStepStartedEvent;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.state.net.ITokenRemovalOperation;
import de.fekl.stat.core.api.state.net.ITokenTransitionOperation;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.token.SimpleToken;
import de.fekl.stat.core.impl.token.SimpleTokenFactory;
import de.fekl.stat.util.ILogger;
import de.fekl.stat.util.LogManager;

public class SimpleColouredNetProcessingContainerTest {

	@Test
	public void testSimpleRouteProcessing() {
		//@formatter:off
		ISpongeNet<SimpleNode> spongeNet = new SpongeNetBuilder<SimpleNode>()
				.setGraph(new DirectedGraphBuilder<SimpleNode>()
					.addEdge("A","B")
					.addEdge("B","C")
					.addEdge("C","D"))
				.setStartNode("A")
		.build();
		//@formatter:on

		SimpleTokenFactory simpleTokenFactory = new SimpleTokenFactory();

		IColouredNetProcessingContainer<SimpleToken> processingContainer = new SimpleColouredNetProcessingContainer<>(
				spongeNet, simpleTokenFactory);

		SimpleToken token = simpleTokenFactory.createToken();
		processingContainer.process(token);
		String position = processingContainer.getCurrentState().getPosition(token);
		Assertions.assertEquals("D", position);
	}

	@Test
	public void testSimpleSpongeProcessing() {
		//@formatter:off
		ISpongeNet<SimpleNode> spongeNet = new SpongeNetBuilder<SimpleNode>()
				.setGraph(new DirectedGraphBuilder<SimpleNode>()
						.addEdge("A","B")
						.addEdge("A","C")
						.addEdge("C","D")
						.addEdge("B","E")
						.addEdge("D","F")
						.addEdge("E","F"))
				.setStartNode("A")
				.build();
		//@formatter:on

		SimpleTokenFactory simpleTokenFactory = new SimpleTokenFactory();

		IColouredNetProcessingContainer<SimpleToken> processingContainer = new SimpleColouredNetProcessingContainer<>(
				spongeNet, simpleTokenFactory);

		SimpleToken token = simpleTokenFactory.createToken();
		processingContainer.process(token);
		String position = processingContainer.getCurrentState().getPosition(token);
		Assertions.assertEquals("F", position);
	}

	@Test
	public void testContainerReset() {
		ISpongeNet<INode> spongeNet = new SpongeNetBuilder<>(new DirectedGraphBuilder<>().addEdge("A", "B")).build();

		SimpleTokenFactory simpleTokenFactory = new SimpleTokenFactory();
		SimpleToken token = simpleTokenFactory.createToken();

		IColouredNetProcessingContainer<SimpleToken> processingContainer;
		String endNode;

		processingContainer = new SimpleColouredNetProcessingContainer<>(spongeNet, simpleTokenFactory);

		processingContainer.process(token);
		endNode = processingContainer.getCurrentState().getPosition(token);
		Assertions.assertEquals("B", endNode);

		processingContainer.reset();
		endNode = processingContainer.getCurrentState().getPosition(token);
		Assertions.assertNull(endNode);

		processingContainer.process(token);
		endNode = processingContainer.getCurrentState().getPosition(token);
		Assertions.assertEquals("B", endNode);
	}

	private static class ValueHolderToken extends SimpleToken {

		String value;

		ValueHolderToken(String id, String value) {
			super(id);
			this.value = value;
		}

		@Override
		public String toString() {
			return String.format("ValueHolder{id:%s, val:%s}", getId(), value);
		}
	}

	private static class ValueHolderTokenFactory implements ITokenFactory<ValueHolderToken> {

		@Override
		public ValueHolderToken createToken(String id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ValueHolderToken copyToken(ValueHolderToken token) {
			return new ValueHolderToken(token.getId() + "_copy", token.value);
		}

		@Override
		public ValueHolderToken mergeToken(List<ValueHolderToken> tokens) {
			StringBuilder idBuilder = new StringBuilder();
			StringBuilder valueBuilder = new StringBuilder();
			for (var t : tokens) {
				idBuilder.append(t.getId());
				idBuilder.append("+");
				valueBuilder.append(t.value);
				valueBuilder.append("+");
			}
			return new ValueHolderToken(idBuilder.toString(), valueBuilder.toString());
		}

	}

	private static abstract class ValueModifierNode extends SimpleNode {

		ValueModifierNode(String id) {
			super(id);
		}

		public abstract String modify(String value);

	}

	private static class ValueHolderProcessingContainer
			extends SimpleColouredNetProcessingContainer<ValueModifierNode, ValueHolderToken> {

		private static final ILogger LOG = LogManager.getInstance().getLogger(ValueHolderProcessingContainer.class);

		public ValueHolderProcessingContainer(ISpongeNet<ValueModifierNode> net) {
			super(net, new ValueHolderTokenFactory());
			onProcessingEvent(e -> {
				LOG.debug(e.getClass().getName());
				if (e instanceof IStepStartedEvent stepStartedEvent) {
					LOG.debug("Processing container %s, step %s with state: %s", "this", stepStartedEvent.getStep(),
							ITokenStore.print(getStateContainer().getCurrentState()));
				} else if (e instanceof IEndNodeReachedEvent<?, ?>endNodeReached) {
					ValueModifierNode node = (ValueModifierNode) endNodeReached.getNode();
					ValueHolderToken token = (ValueHolderToken) endNodeReached.getToken();
					token.value = node.modify(token.value);
				}
			});
			onStateChangeEvent(e -> {
				LOG.debug(e.getClass().getName());
				if (e instanceof IStateHasChangedEvent<?>stateChangedEvent) {
					if (stateChangedEvent.getSourceOperation() instanceof ITokenTransitionOperation<?>transition) {
						String sourceNodeId = transition.getSourceNodeId();
						ValueModifierNode node = getNet().getNode(sourceNodeId);
						ValueHolderToken transitionedToken = (ValueHolderToken) transition.getTransitionedToken();
						transitionedToken.value = node.modify(transitionedToken.value);
					} else if (stateChangedEvent.getSourceOperation() instanceof ITokenRemovalOperation<?>removal) {
						String sourceNodeId = removal.getTargetNodeId();
						ValueModifierNode node = getNet().getNode(sourceNodeId);
						ValueHolderToken transitionedToken = (ValueHolderToken) removal.getRemovedToken();
						transitionedToken.value = node.modify(transitionedToken.value);
					}
				}
			});
		}
	}

	@Test
	public void testValueHolderProcessing() {
		//@formatter:off
		ISpongeNet<ValueModifierNode> spongeNet = new SpongeNetBuilder<ValueModifierNode>()
				.setGraph(new DirectedGraphBuilder<ValueModifierNode>()
					.addNode(new ValueModifierNode("A") {
						
						@Override
						public String modify(String value) {
							return value+"A";
						}
					})
					.addNode(new ValueModifierNode("B") {
						
						@Override
						public String modify(String value) {
							return value+"B";
						}
					})
					.addNode(new ValueModifierNode("C") {
						
						@Override
						public String modify(String value) {
							return value+"C";
						}
					})
					.addEdge("A","B")
					.addEdge("B","C"))
				.setStartNode("A")
		.build();
		//@formatter:on

		ValueHolderProcessingContainer processingContainer = new ValueHolderProcessingContainer(spongeNet);

		ValueHolderToken valueHolderToken = new ValueHolderToken("initId", "hello");

		processingContainer.process(valueHolderToken);
		String endNode = processingContainer.getCurrentState().getPosition(valueHolderToken);
		Assertions.assertEquals("C", endNode);
		Assertions.assertEquals("helloABC", valueHolderToken.value);

	}

}
