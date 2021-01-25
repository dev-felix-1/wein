package de.fekl.stat.core.impl.state.net;

import java.util.ArrayList;
import java.util.List;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;

public class SimpleColouredNetProcessor {

	protected <N extends INode, T extends IToken> IColouredNetProcessingContainer<T> createContainer(ISpongeNet<N> net,
			ITokenFactory<T> tokenFactory) {
		return new SimpleColouredNetProcessingContainer<>(net, tokenFactory);
	}

	public <N extends INode, T extends IToken> T processSingleResult(ISpongeNet<N> net, T input,
			ITokenFactory<T> tokenFactory) {
		var processingContainer = createContainer(net, tokenFactory);
//		CountDownLatch waitForFinish = new CountDownLatch(1);
//		processingContainer.onFinish(e -> waitForFinish.countDown());
		processingContainer.process(input);
		return processingContainer.getCurrentState().getTokens().iterator().next();
	}
	
	public <N extends INode, T extends IToken> List<T> processMultiResult(ISpongeNet<N> net, T input,
			ITokenFactory<T> tokenFactory) {
		var processingContainer = createContainer(net, tokenFactory);
//		CountDownLatch waitForFinish = new CountDownLatch(1);
//		processingContainer.onFinish(e -> waitForFinish.countDown());
		processingContainer.process(input);
		return new ArrayList<>(processingContainer.getCurrentState().getTokens());
	}

	public <N extends INode, T extends IToken> T processSingleResult(ISpongeNet<N> net, T input,
			ITokenFactory<T> tokenFactory, IEventListener<IStateHasChangedEvent<T>> onStateChange) {
		var processingContainer = createContainer(net, tokenFactory);
//		CountDownLatch waitForFinish = new CountDownLatch(1);
//		processingContainer.onFinish(e -> waitForFinish.countDown());
		processingContainer.onStateChangedEvent(onStateChange);
		processingContainer.process(input);
		return processingContainer.getCurrentState().getTokens().iterator().next();
	}

}
