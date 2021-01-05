package de.fekl.dine.api.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fekl.baut.Precondition;
import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.graph.IDirectedGraph;

public class SimpleTreeNet implements ITreeNet {

	private final IDirectedGraph graph;
	private final String startNode;

	public SimpleTreeNet(IDirectedGraph graph, String startNode) {
		Precondition.isNotNull(graph);
		Precondition.isNotEmpty(startNode);
		if (!graph.hasNode(startNode)) {
			throw new IllegalArgumentException("startNode is not part of the graph");
		}
		if (!isConnected(graph)) {
			throw new IllegalArgumentException("graph is not fully connected");
		}
		if (isCyclic(graph)) {
			throw new IllegalArgumentException("graph is cyclic");
		}
		this.graph = graph;
		this.startNode = startNode;
	}

	private static boolean isConnected(IDirectedGraph graph) {
		String next = graph.getNodes().iterator().next();
		Set<String> collectChildren = collectChildren(graph, next);
		return collectChildren.size() == graph.getNodes().size();
	}

	private static void collectChildren(IDirectedGraph graph, String startNode, Set<String> visited) {
		visited.add(startNode);
		for (IEdge edge : graph.getOutgoingEdges(startNode)) {
			if (visited.contains(edge.getTarget())) {
				return;
			} else {
				collectChildren(graph, edge.getTarget(), visited);
			}
		}
	}

	private static Set<String> collectChildren(IDirectedGraph graph, String startNode) {
		Set<String> children = new HashSet<>();
		collectChildren(graph, startNode, children);
		return children;
	}

	private static void collectLeafs(IDirectedGraph graph, String startNode, Set<String> visited) {
		List<IEdge> outgoingEdges = graph.getOutgoingEdges(startNode);
		if (outgoingEdges.isEmpty()) {
			visited.add(startNode);
		} else {
			for (IEdge edge : outgoingEdges) {
				collectLeafs(graph, edge.getTarget(), visited);
			}
		}
	}

	private static Set<String> collectLeafs(IDirectedGraph graph, String startNode) {
		Set<String> leafs = new HashSet<>();
		collectLeafs(graph, startNode, leafs);
		return leafs;
	}

	private static boolean isCyclic(IDirectedGraph graph) {
		String next = graph.getNodes().iterator().next();
		return hasCycle(graph, next);
	}

	private static boolean hasCycle(IDirectedGraph graph, String startNode, Set<String> visited) {
		visited.add(startNode);
		for (IEdge edge : graph.getOutgoingEdges(startNode)) {
			if (visited.contains(edge.getTarget())) {
				return true;
			} else {
				return hasCycle(graph, edge.getTarget(), visited);
			}
		}
		return false;
	}

	private static boolean hasCycle(IDirectedGraph graph, String startNode) {
		return hasCycle(graph, startNode, new HashSet<>());
	}

	@Override
	public Set<String> getNodes() {
		return graph.getNodes();
	}

	@Override
	public List<IEdge> getEdges() {
		return graph.getEdges();
	}

	@Override
	public boolean hasNode(String name) {
		return graph.hasNode(name);
	}

	@Override
	public List<IEdge> getIncomingEdges(String nodeName) {
		return graph.getIncomingEdges(nodeName);
	}

	@Override
	public List<IEdge> getOutgoingEdges(String nodeName) {
		return graph.getOutgoingEdges(nodeName);
	}

	@Override
	public String getRoot() {
		return startNode;
	}

	@Override
	public boolean isRoot(String nodeName) {
		return startNode.equals(nodeName);
	}

	@Override
	public Set<String> getLeafs() {
		return collectLeafs(graph, startNode);
	}

	@Override
	public boolean isLeaf(String nodeName) {
		return collectLeafs(graph, startNode).contains(nodeName);
	}

}
