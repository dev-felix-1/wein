package de.fekl.dine.api.graph;

import java.util.List;

import de.fekl.dine.api.core.IEdge;

public interface IDirectedGraphFactory {

	IDirectedGraph createDirectedGraph(List<String> nodes, List<IEdge> edges);

}
