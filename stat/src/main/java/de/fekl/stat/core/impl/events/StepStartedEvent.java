package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.IStepStartedEvent;

public class StepStartedEvent implements IStepStartedEvent {

	private final long step;

	public StepStartedEvent(long step) {
		this.step = step;
	}

	@Override
	public long getStep() {
		return step;
	}

}
