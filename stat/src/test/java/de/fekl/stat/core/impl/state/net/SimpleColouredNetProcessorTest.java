package de.fekl.stat.core.impl.state.net;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.api.sponge.SpongeNetBuilder;
import de.fekl.dine.core.impl.node.SimpleNode;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.state.operations.ITokenCreationOperation;
import de.fekl.stat.core.api.state.operations.ITokenTransitionOperation;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.token.SimpleToken;
import de.fekl.stat.core.impl.token.SimpleTokenFactory;

public class SimpleColouredNetProcessorTest {

	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testProcessSingleResult() {
		//@formatter:off
		ISpongeNet<SimpleNode> spongeNet = new SpongeNetBuilder<SimpleNode>()
			.setGraph(new DirectedGraphBuilder<SimpleNode>()
				.addEdge("A","B")
				.addEdge("B","C")
				.addEdge("C","D"))
		.build();
		//@formatter:on
		var processor = new SimpleColouredNetProcessor();
		var simpleTokenFactory = new SimpleTokenFactory();
		var processSingleResult = processor.processSingleResult(spongeNet, simpleTokenFactory.createToken("hi"),
				simpleTokenFactory);
		Assertions.assertEquals("hi", processSingleResult.getId());
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
			return new ValueHolderToken(id, "empty");
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

	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testProcessSingleResultWithCustomNodesAndTokens() {
		//@formatter:off
		ISpongeNet<ValueModifierNode> spongeNet = new SpongeNetBuilder<ValueModifierNode>()
			.setGraph(new DirectedGraphBuilder<ValueModifierNode>()
				.addNode(new ValueModifierNode("A"))
				.addNode(new ValueModifierNode("B"))
				.addNode(new ValueModifierNode("C"))
				.addEdge("A","B")
				.addEdge("B","C"))
		.build();
		//@formatter:on
		var processor = new SimpleColouredNetProcessor();
		var tokenFactory = new ValueHolderTokenFactory();

		IEventListener<IStateHasChangedEvent<ITokenStore<ValueHolderToken>, IStateChangeOperation<ITokenStore<ValueHolderToken>>>> stateChangedEventHandler = (
				e) -> {
			if (ITokenTransitionOperation.class.isAssignableFrom(e.getSourceOperation().getClass())) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				var transition = (ITokenTransitionOperation<ValueHolderToken>) (ITokenTransitionOperation) e
						.getSourceOperation();
				var node = spongeNet.getNode(transition.getTargetNodeId());
				var tokn = transition.getTransitionedToken();
				tokn.value = node.modify(tokn.value);
			} else if (ITokenCreationOperation.class.isAssignableFrom(e.getSourceOperation().getClass())) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				var creation = (ITokenCreationOperation<ValueHolderToken>) (ITokenCreationOperation) e
						.getSourceOperation();
				var node = spongeNet.getNode(creation.getTargetNodeId());
				var token = creation.getCreatedToken();
				token.value = node.modify(token.value);
			}
		};

		var processSingleResult = processor.processSingleResult(spongeNet, tokenFactory.createToken("hi"), tokenFactory,
				stateChangedEventHandler);
		Assertions.assertEquals("hi", processSingleResult.getId());
		Assertions.assertEquals("emptyABC", processSingleResult.value);
	}

}
