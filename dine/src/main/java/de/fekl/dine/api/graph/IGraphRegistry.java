package de.fekl.dine.api.graph;

import java.util.Map;

import de.fekl.dine.api.node.INode;

public interface IGraphRegistry<N extends INode, G extends IGraph<N>> {

	public String register(G graph);

	public void register(String graphId, G graph);

	public void unRegister(String graphId);

	public G find(String graphId);

	public boolean contains(String graphId);

	public Map<String, G> getMap();

}
