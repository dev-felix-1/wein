package de.fekl.stat.core.impl.state.net;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.util.Precondition;
import de.fekl.stat.core.api.edge.conditional.ICondition;
import de.fekl.stat.core.api.edge.conditional.IConditionalEdge;
import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventBus;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IProcessFinishedEvent;
import de.fekl.stat.core.api.events.IProcessStartedEvent;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.events.ITokenCreationEvent;
import de.fekl.stat.core.api.events.ITokenMergeEvent;
import de.fekl.stat.core.api.events.ITokenTransitionEvent;
import de.fekl.stat.core.api.node.IAutoMergeNode;
import de.fekl.stat.core.api.node.IAutoSplitNode;
import de.fekl.stat.core.api.state.IStateContainer;
import de.fekl.stat.core.api.state.net.IColouredNetProcessingContainer;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.edge.conditional.SimpleConditionEvaluationContext;

public abstract class AbstractColouredNetProcessingContainer<T extends IToken, N extends INode>
		implements IColouredNetProcessingContainer<T> {

	private final IStateContainer<ITokenStore<T>> stateContainer;
	private final ISpongeNet<N> net;
	private final IEventBus<IEvent> stateChangedEventBus;
	private final IEventBus<IEvent> processingEventBus;
	private final ITokenFactory<T> tokenFactory;

	public AbstractColouredNetProcessingContainer(ISpongeNet<N> net, IStateContainer<ITokenStore<T>> stateContainer,
			ITokenFactory<T> tokenFactory, IEventBus<IEvent> stateChangedEventBus,
			IEventBus<IEvent> processingEventBus) {
		Precondition.isNotNull(net);
		Precondition.isNotNull(tokenFactory);
		Precondition.isNotNull(stateChangedEventBus);
		Precondition.isNotNull(processingEventBus);
		Precondition.isNotNull(stateContainer);
		this.net = net;
		this.tokenFactory = tokenFactory;
		this.stateChangedEventBus = stateChangedEventBus;
		this.processingEventBus = processingEventBus;
		this.stateContainer = stateContainer;
		this.stateContainer.setEventBus(stateChangedEventBus);
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

	protected Map<String, String> getTokenPositions() {
		return getCurrentState().getTokenPositions();
	}

	protected boolean isSplitterNode(N node) {
		return node instanceof IAutoSplitNode && getNet().getOutgoingEdges(node).size() > 1;
	}

	protected boolean isMergerNode(N node) {
		return node instanceof IAutoMergeNode && getNet().getIncomingEdges(node).size() > 1;
	}

	protected void changeState(IStateChangeOperation<ITokenStore<T>> stateChangeOperation) {
		getStateContainer().changeState(stateChangeOperation);
	}

	protected boolean isConditionalEdge(IEdge edge) {
		return edge instanceof IConditionalEdge<?, ?>;
	}

	@SuppressWarnings("unchecked")
	protected boolean evaluateEdgeCondition(N sourceNode, N targetNode, T token, IEdge edge) {
		ICondition<N, T> condition = ((IConditionalEdge<N, T>) edge).getCondition();
		return condition.evaluate(
				new SimpleConditionEvaluationContext<>(sourceNode, targetNode, token, (IConditionalEdge<N, T>) edge));
	}

	protected boolean edgeCanTransition(T token, IEdge edge) {
		if (isConditionalEdge(edge)) {
			String target = edge.getTarget();
			String source = edge.getSource();
			N sourceNode = getNet().getNode(source);
			N targetNode = getNet().getNode(target);
			return evaluateEdgeCondition(sourceNode, targetNode, token, edge);
		} else {
			return true;
		}
	}

	protected boolean isFinished() {
		return getTokenPositions().entrySet().stream().allMatch(e -> getNet().isLeaf(e.getValue()));
	}

	protected List<IEdge> filterTransitionReadyEdges(List<IEdge> edges, T token) {
		return edges.stream().filter(e -> edgeCanTransition(token, e)).collect(Collectors.toList());
	}

}
