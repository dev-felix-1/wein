package de.fekl.dine.api.base;

import de.fekl.baut.Precondition;

public abstract class AbstractIdHolder<I> implements IIdHolder<I> {

	private final I id;

	public AbstractIdHolder(I id) {
		Precondition.isNotEmpty(id);
		this.id = id;
	}

	@Override
	public I getId() {
		return id;
	}

}
