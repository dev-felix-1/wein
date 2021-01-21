package de.fekl.dine.util;

import de.fekl.dine.core.api.base.IBuilder;
import de.fekl.dine.core.api.base.IFactory;
import de.fekl.dine.core.api.base.IIdHolder;
import de.fekl.dine.core.api.base.IRegistry;

public abstract class AbstractBuilder<I, N extends IIdHolder<I>, F extends IFactory<I, N>, A extends AbstractBuilder<I, N, F, A>>
		implements IBuilder<I, N, F, A> {

	private I id;

	private boolean autoRegister;
	private boolean autoLookUp;
	private IRegistry<I, N> registry;

	@SuppressWarnings("unchecked")
	@Override
	public A id(I id) {
		this.id = id;
		return (A) this;
	}

	protected I getId() {
		return id;
	}

	protected abstract N doBuild();

	protected N doLookUp() {
		return registry.get(getId());
	}

	@Override
	public N build() {
		N result = null;
		if (autoLookUp == true && registry != null) {
			result = doLookUp();
		}
		if (result == null) {
			result = doBuild();
		}
		if (autoRegister == true && registry != null) {
			registry.register(result);
		}
		return result;
	}

	public boolean isAutoRegister() {
		return autoRegister;
	}

	public boolean isAutoLookUp() {
		return autoLookUp;
	}

	@SuppressWarnings("unchecked")
	public A setAutoRegister(boolean autoRegister) {
		this.autoRegister = autoRegister;
		return (A) this;
	}

	@SuppressWarnings("unchecked")
	public A setAutoLookUp(boolean autoLookUp) {
		this.autoLookUp = autoLookUp;
		return (A) this;
	}

	@SuppressWarnings("unchecked")
	public A setRegistry(IRegistry<I, N> registry) {
		this.registry = registry;
		return (A) this;
	}

	public IRegistry<I, N> getRegistry() {
		return registry;
	}

}
