package de.fekl.stat.core.impl.events;

import de.fekl.stat.core.api.events.IStepFinishedEvent;

public class StepFinishedEvent implements IStepFinishedEvent {
	private final long step;

	public StepFinishedEvent(long step) {
		this.step = step;
	}

	@Override
	public long getStep() {
		return step;
	}
}
