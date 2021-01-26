package de.fekl.stat.core.impl.state.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.fekl.dine.core.api.graph.IGraph;
import de.fekl.dine.core.api.graph.IGraphRegistry;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;

public class SimpleColouredNetProcessor {

	private Map<String, IColouredNetProcessingContainer<?>> containers = new HashMap<>();
	private IGraphRegistry<?, IGraph<?>> netRegistry;

	public void setNetRegistry(IGraphRegistry<?, IGraph<?>> netRegistry) {
		this.netRegistry = netRegistry;
	}

	protected <N extends INode, T extends IToken> IColouredNetProcessingContainer<T> createContainer(ISpongeNet<N> net,
			ITokenFactory<T> tokenFactory) {
		return new SimpleColouredNetProcessingContainer<>(net, tokenFactory);
	}

	public <N extends INode, T extends IToken> T processSingleResult(ISpongeNet<N> net, T input,
			ITokenFactory<T> tokenFactory) {
		var processingContainer = createContainer(net, tokenFactory);
		processingContainer.process(input);
		return processingContainer.getCurrentState().getTokens().iterator().next();
	}

	public <N extends INode, T extends IToken> T processSingleResult(ISpongeNet<N> net, T input,
			ITokenFactory<T> tokenFactory,
			IEventListener<IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>> onStateChange) {
		var processingContainer = createContainer(net, tokenFactory);
		processingContainer.onStateChangedEvent(onStateChange);
		processingContainer.process(input);
		return processingContainer.getCurrentState().getTokens().iterator().next();
	}
	
	public <N extends INode, T extends IToken> List<T> processMultiResult(ISpongeNet<N> net, T input,
			ITokenFactory<T> tokenFactory) {
		var processingContainer = createContainer(net, tokenFactory);
		processingContainer.process(input);
		return new ArrayList<>(processingContainer.getCurrentState().getTokens());
	}

	public <N extends INode, T extends IToken> List<T> processMultiResult(ISpongeNet<N> net, T input,
			ITokenFactory<T> tokenFactory,
			IEventListener<IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>> onStateChange) {
		var processingContainer = createContainer(net, tokenFactory);
		processingContainer.onStateChangedEvent(onStateChange);
		processingContainer.process(input);
		return new ArrayList<>(processingContainer.getCurrentState().getTokens());
	}

}
