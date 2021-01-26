package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.IStateHasChangedEvent;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;

public abstract class AbstractStateHasChangedEvent<S, O extends IStateChangeOperation<S>>
		implements IStateHasChangedEvent<S, O> {

	private final O sourceOperation;
	private final S sourceState;
	private final S targetState;

	public AbstractStateHasChangedEvent(O sourceOperation, S sourceState, S targetState) {
		super();
		this.sourceOperation = sourceOperation;
		this.sourceState = sourceState;
		this.targetState = targetState;
	}

	@Override
	public O getSourceOperation() {
		return sourceOperation;
	}

	@Override
	public S getSourceState() {
		return sourceState;
	}

	@Override
	public S getTargetState() {
		return targetState;
	}

	public static class DefaultEvent<S, O extends IStateChangeOperation<S>> extends AbstractStateHasChangedEvent<S, O> {

		public DefaultEvent(O sourceOperation, S sourceState, S targetState) {
			super(sourceOperation, sourceState, targetState);
		}

	}

}
