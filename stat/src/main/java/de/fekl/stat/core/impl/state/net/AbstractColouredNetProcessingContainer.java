package de.fekl.stat.core.impl.state.net;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IProcessFinishedEvent;
import de.fekl.stat.core.api.events.IProcessStartedEvent;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.events.ITokenCreationEvent;
import de.fekl.stat.core.api.events.ITokenMergeEvent;
import de.fekl.stat.core.api.events.ITokenTransitionEvent;
import de.fekl.stat.core.api.state.IStateContainer;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.events.SimpleEventBus;
import de.fekl.stat.core.impl.state.TokenStoreStateContainer;

public abstract class AbstractColouredNetProcessingContainer<T extends IToken, N extends INode>
		implements IColouredNetProcessingContainer<T> {

	private final IStateContainer<ITokenStore<T>> stateContainer;
	private final ISpongeNet<N> net;
	private final IEventBus<IEvent> stateChangedEventBus;
	private final IEventBus<IEvent> processingEventBus;

	private ITokenFactory<T> tokenFactory;

	public AbstractColouredNetProcessingContainer(ISpongeNet<N> net, ITokenStore<T> initialState,
			ITokenFactory<T> tokenFactory) {
		this.net = net;
		this.tokenFactory = tokenFactory;
		stateChangedEventBus = new SimpleEventBus<>();
		processingEventBus = new SimpleEventBus<>();
		stateContainer = new TokenStoreStateContainer<>(initialState, stateChangedEventBus);
	}

	@Override
	public ITokenStore<T> getCurrentState() {
		return stateContainer.getCurrentState();
	}

	@Override
	public void onFinish(IEventListener<IProcessFinishedEvent> listener) {
		processingEventBus.register(IProcessFinishedEvent.class, listener);
	}

	@Override
	public void onStart(IEventListener<IProcessStartedEvent> listener) {
		processingEventBus.register(IProcessStartedEvent.class, listener);
	}

	@Override
	public void onProcessingEvent(IEventListener<IEvent> listener) {
		processingEventBus.register(listener);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onStateChangedEvent(
			IEventListener<IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>> listener) {
		stateChangedEventBus.register(
				(Class<IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>>) (Class<?>) IStateHasChangedEvent.class,
				listener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onTokenCreation(IEventListener<ITokenCreationEvent<T>> listener) {
		stateChangedEventBus.register(ITokenCreationEvent.class,
				(IEventListener<ITokenCreationEvent>) (IEventListener<?>) listener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onTokenTransition(IEventListener<ITokenTransitionEvent<T>> listener) {
		stateChangedEventBus.register(ITokenTransitionEvent.class,
				(IEventListener<ITokenTransitionEvent>) (IEventListener<?>) listener);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onTokenMerge(IEventListener<ITokenMergeEvent<T>> listener) {
		stateChangedEventBus.register(ITokenMergeEvent.class,
				(IEventListener<ITokenMergeEvent>) (IEventListener<?>) listener);

	}

	protected <E extends IEvent> void onProcessingEvent(Class<E> type, IEventListener<E> listener) {
		processingEventBus.register(type, listener);
	}

	protected ITokenFactory<T> getTokenFactory() {
		return tokenFactory;
	}

	protected ISpongeNet<N> getNet() {
		return net;
	}

	protected IStateContainer<ITokenStore<T>> getStateContainer() {
		return stateContainer;
	}

	protected void postProcessingEvent(IEvent event) {
		processingEventBus.post(event);
	}

	protected <E extends IEvent> void onStateChangeEvent(Class<E> type, IEventListener<E> listener) {
		stateChangedEventBus.register(type, listener);
	}

}
