package de.fekl.dine.api.graph;

import java.util.List;
import java.util.Set;

import de.fekl.dine.api.core.IEdge;

public interface IDirectedGraphFactory {

	IDirectedGraph createDirectedGraph(Set<String> nodes, List<IEdge> edges);

}
