package de.fekl.tran.impl;

import java.util.HashMap;
import java.util.Map;

import de.fekl.baut.Precondition;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.api.core.ITransformerRegistry;

public class SimpleTransformerRegistry implements ITransformerRegistry {

	@SuppressWarnings("rawtypes")
	private final Map<String, ITransformer> map = new HashMap<>();

	@Override
	public synchronized <S,T> void register(ITransformer<S,T> transformer) {
		Precondition.isNotNull(transformer);
		Precondition.isNotEmpty(transformer.getId());
		if (map.containsKey(transformer.getId())) {
			throw new IllegalStateException(String.format("There is already an registry entry for transformer %s", transformer.getId()));
		}
		map.put(transformer.getId(), transformer);
	}

	@Override
	public synchronized void unRegister(String id) {
		map.remove(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S,T> ITransformer<S,T> getTransformer(String id) {
		return map.get(id);
	}

	@Override
	public boolean contains(String id) {
		return map.containsKey(id);
	}

}
