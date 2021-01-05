package de.fekl.dine.api.state;

import java.util.Map;
import java.util.Set;

import de.fekl.dine.api.graph.IDirectedGraph;

public interface IColouredGraph {

	IDirectedGraph getGraph();

	void putToken(String nodeId, IToken token);

	Set<IToken> getTokens(String nodeId);

	Map<String, IToken> getTokenMapping();

}
