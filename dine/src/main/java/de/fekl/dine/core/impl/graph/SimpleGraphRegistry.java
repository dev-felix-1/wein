package de.fekl.dine.core.impl.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.fekl.dine.core.api.graph.IGraph;
import de.fekl.dine.core.api.graph.IGraphRegistry;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.util.Precondition;

public class SimpleGraphRegistry<N extends INode, G extends IGraph<N>> implements IGraphRegistry<N, G> {

	private final Map<String, G> map = new HashMap<>();

	@Override
	public synchronized void register(G graph) {
		Precondition.isNotNull(graph);
		map.put(graph.getId(), graph);
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
	public synchronized void unRegister(String graphId) {
		Precondition.isNotEmpty(graphId);
		map.remove(graphId);
	}

}
