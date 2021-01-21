package de.fekl.dine.util;

import java.util.HashMap;
import java.util.Map;

import de.fekl.dine.core.api.base.IIdHolder;
import de.fekl.dine.core.api.base.IRegistry;

public abstract class AbstractRegistry<I, N extends IIdHolder<I>> implements IRegistry<I, N> {

	private final Map<I, N> map = new HashMap<>();

	@Override
	public synchronized void register(N node) {
		Precondition.isNotNull(node);
		Precondition.isNotEmpty(node.getId());
		if (map.containsKey(node.getId())) {
			throw new IllegalStateException(String.format(
					"Cannot register node with id %s: there is already a node with this id registered", node.getId()));
		}
		map.put(node.getId(), node);
	}

	@Override
	public synchronized void unRegister(I id) {
		Precondition.isNotEmpty(id);
		map.remove(id);
	}

	@Override
	public N get(I id) {
		Precondition.isNotEmpty(id);
		return map.get(id);
	}

	@Override
	public boolean contains(I id) {
		Precondition.isNotEmpty(id);
		return map.containsKey(id);
	}

	public static final class DefaultRegistryImpl<I, N extends IIdHolder<I>> extends AbstractRegistry<I, N> {

	}

}
