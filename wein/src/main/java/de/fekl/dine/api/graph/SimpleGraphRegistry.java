package de.fekl.dine.api.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.fekl.baut.Precondition;

public class SimpleGraphRegistry<N extends INode, G extends IGraph<N>> implements IGraphRegistry<N, G> {

	private final Map<String, G> map = new HashMap<>();

	@Override
	public synchronized String register(G graph) {
		Precondition.isNotNull(graph);
		String generatedName;
		do {
			generatedName = GraphNames.generateName();
		} while (contains(generatedName));
		map.put(generatedName, graph);
		return generatedName;
	}

	@Override
	public G find(String graphId) {
		return map.get(graphId);
	}

	@Override
	public boolean contains(String graphId) {
		return map.containsKey(graphId);
	}

	@Override
	public Map<String, G> getMap() {
		return Collections.unmodifiableMap(map);
	}

	@Override
	public synchronized void register(String graphId, G graph) {
		Precondition.isNotEmpty(graphId);
		Precondition.isNotNull(graph);
		if (contains(graphId)) {
			throw new IllegalArgumentException(String.format("The id %s is already associated with a graph", graph));
		}
		map.put(graphId, graph);
	}

	@Override
	public synchronized void unRegister(String graphId) {
		Precondition.isNotEmpty(graphId);
		map.remove(graphId);
	}

}
