package de.fekl.dine.util;

import de.fekl.dine.core.api.base.IIdHolder;

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
