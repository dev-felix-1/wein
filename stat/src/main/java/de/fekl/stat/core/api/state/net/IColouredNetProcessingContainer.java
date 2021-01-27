package de.fekl.stat.core.api.state.net;

import de.fekl.stat.core.api.events.IEvent;
import de.fekl.stat.core.api.events.IEventListener;
import de.fekl.stat.core.api.events.IProcessFinishedEvent;
import de.fekl.stat.core.api.events.IProcessStartedEvent;
import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.events.ITokenCreationEvent;
import de.fekl.stat.core.api.events.ITokenMergeEvent;
import de.fekl.stat.core.api.events.ITokenTransitionEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;

public interface IColouredNetProcessingContainer<T extends IToken> {

	void process(T token);

	ITokenStore<T> getCurrentState();

	void reset();

	boolean isRunning();

	void onFinish(IEventListener<IProcessFinishedEvent> listener);

	void onStart(IEventListener<IProcessStartedEvent> listener);

	void onTokenCreation(IEventListener<ITokenCreationEvent<T>> listener);

	void onTokenTransition(IEventListener<ITokenTransitionEvent<T>> listener);

	void onTokenMerge(IEventListener<ITokenMergeEvent<T>> listener);

	void onStateChangedEvent(
			IEventListener<IStateHasChangedEvent<ITokenStore<T>, IStateChangeOperation<ITokenStore<T>>>> listener);

	void onProcessingEvent(IEventListener<IEvent> listener);

	void update();

	boolean isWaiting();
}
