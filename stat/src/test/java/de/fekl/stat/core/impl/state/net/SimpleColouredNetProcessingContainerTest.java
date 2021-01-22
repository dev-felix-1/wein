package de.fekl.stat.core.impl.state.net;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.api.sponge.SpongeNetBuilder;
import de.fekl.dine.core.impl.node.SimpleNode;
import de.fekl.stat.core.api.events.IProcessFinishedEvent;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.events.IStepStartedEvent;
import de.fekl.stat.core.api.node.IAutoSplitNode;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.state.net.ITokenCreationOperation;
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

	private static class ValueModifierNode extends SimpleNode {

		ValueModifierNode(String id) {
			super(id);
		}

		public String modify(String value) {
			return value + getId();
		}

	}

	private static class ValueHolderProcessingContainer
			extends SimpleColouredNetProcessingContainer<ValueModifierNode, ValueHolderToken> {

		private static final ILogger LOG = LogManager.getInstance().getLogger(ValueHolderProcessingContainer.class);

		public ValueHolderProcessingContainer(ISpongeNet<ValueModifierNode> net) {
			super(net, new ValueHolderTokenFactory());
			onProcessingEvent(IStepStartedEvent.class, e -> {
				LOG.debug(e.getClass().getName());
				LOG.debug("Processing container %s, step %s with state: %s", "this", e.getStep(),
						ITokenStore.print(getStateContainer().getCurrentState()));
			});
			onStateChangeEvent(IStateHasChangedEvent.class, e -> {
				LOG.debug(e.getClass().getName());
				if (e.getSourceOperation() instanceof ITokenTransitionOperation<?>transition) {
					String id = transition.getTargetNodeId();
					ValueModifierNode node = getNet().getNode(id);
					ValueHolderToken tokn = (ValueHolderToken) transition.getTransitionedToken();
					tokn.value = node.modify(tokn.value);
				} else if (e.getSourceOperation() instanceof ITokenCreationOperation<?>creation) {
					String id = creation.getTargetNodeId();
					ValueModifierNode node = getNet().getNode(id);
					ValueHolderToken token = (ValueHolderToken) creation.getCreatedToken();
					token.value = node.modify(token.value);
				}
			});
		}
	}

	@Test
	public void testValueHolderProcessing() {
		//@formatter:off
		ISpongeNet<ValueModifierNode> spongeNet = new SpongeNetBuilder<ValueModifierNode>()
				.setGraph(new DirectedGraphBuilder<ValueModifierNode>()
					.addNode(new ValueModifierNode("A"))
					.addNode(new ValueModifierNode("B"))
					.addNode(new ValueModifierNode("C"))
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

	private static class SplitterNode extends ValueModifierNode implements IAutoSplitNode {

		SplitterNode(String id) {
			super(id);
		}
	}

	private static class MultiResultProcessingContainer extends ValueHolderProcessingContainer {

		private static class LatchHolder {
			private CountDownLatch latch;
		}

		private LatchHolder latchHolder = new LatchHolder();

		public MultiResultProcessingContainer(ISpongeNet<ValueModifierNode> net) {
			super(net);
			onProcessingFinished(e -> latchHolder.latch.countDown());
		}

		List<ValueHolderToken> processForMultiResult(ValueHolderToken token) throws InterruptedException {
			latchHolder.latch = new CountDownLatch(1);
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.submit(() -> process(token));
			latchHolder.latch.await();
			return getCurrentState().getTokenPositions().keySet().stream().map(k -> getCurrentState().getToken(k))
					.collect(Collectors.toList());
		}

	}

	@Test
	public void testMultiResultProcessing() throws InterruptedException {
		//@formatter:off
		ISpongeNet<ValueModifierNode> spongeNet = new SpongeNetBuilder<ValueModifierNode>()
				.setGraph(new DirectedGraphBuilder<ValueModifierNode>()
					.addNode(new SplitterNode("A"))
					.addNode(new ValueModifierNode("B"))
					.addNode(new ValueModifierNode("C"))
					.addEdge("A","B")
					.addEdge("A","C"))
		.build();
		//@formatter:on

		MultiResultProcessingContainer processingContainer = new MultiResultProcessingContainer(spongeNet);

		ValueHolderToken valueHolderToken = new ValueHolderToken("initId", "hello");
		List<ValueHolderToken> processForMultiResult = processingContainer.processForMultiResult(valueHolderToken);

		Assertions.assertEquals(2, processForMultiResult.size());
		Assertions.assertTrue(processForMultiResult.get(0).value.equals("helloAB"));
		Assertions.assertTrue(processForMultiResult.get(1).value.equals("helloAC"));

	}

}
