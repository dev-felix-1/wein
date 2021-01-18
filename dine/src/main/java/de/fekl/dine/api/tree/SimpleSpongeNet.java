package de.fekl.dine.api.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.baut.Precondition;
import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.graph.IDirectedGraph;
import de.fekl.dine.api.node.INode;

public class SimpleSpongeNet<N extends INode> implements ISpongeNet<N> {

	private final IDirectedGraph<N> graph;
	private final String startNode;

	// caching
	private Set<N> leafs;

	public SimpleSpongeNet(IDirectedGraph<N> graph, String startNode) {
		Precondition.isNotNull(graph, "Parameter %s is null", "graph");
		Precondition.isNotEmpty(startNode, "Parameter %s is null", "startNode");
		if (!graph.contains(startNode)) {
			throw new IllegalArgumentException("startNode is not part of the graph");
		}
		checkIsConnected(graph, startNode);
		if (isCyclic(graph)) {
			throw new IllegalArgumentException("graph is cyclic");
		}
		this.graph = graph;
		this.startNode = startNode;
	}

	private static <N extends INode> void checkIsConnected(IDirectedGraph<N> graph, String startNode) {
		Set<String> collectChildren = collectChildrenUndirected(graph, startNode);
		if (collectChildren.size() != graph.getNodes().size()) {
			ArrayList<String> nodeIds = new ArrayList<>(graph.getNodeIds());
			nodeIds.removeAll(collectChildren);
			throw new IllegalArgumentException(
					String.format("Found %s unconnected nodes: %s", nodeIds.size(), nodeIds));
		}
	}

	private static <N extends INode> void collectChildrenUndirected(IDirectedGraph<N> graph, String startNode,
			Set<String> visited) {
		String currentOut = collectChildrenOnewayOutgoingPath(graph, startNode, visited);
		for (IEdge edge : graph.getOutgoingEdges(currentOut)) {
			if (!visited.contains(edge.getTarget())) {
				collectChildrenUndirected(graph, edge.getTarget(), visited);
			}
		}
		String currentIn = collectChildrenOnewayIncomingPath(graph, startNode, visited);
		for (IEdge edge : graph.getIncomingEdges(currentIn)) {
			if (!visited.contains(edge.getSource())) {
				collectChildrenUndirected(graph, edge.getSource(), visited);
			}
		}
	}

	private static <N extends INode> String collectChildrenOnewayOutgoingPath(IDirectedGraph<N> graph, String startNode,
			Set<String> visited) {
		String current = startNode;
		List<IEdge> outgoingEdges = null;
		while ((outgoingEdges = graph.getOutgoingEdges(current)).size() == 1) {
			visited.add(current);
			current = outgoingEdges.get(0).getTarget();
		}
		visited.add(current);
		return current;
	}

	private static <N extends INode> String collectChildrenOnewayIncomingPath(IDirectedGraph<N> graph, String startNode,
			Set<String> visited) {
		String current = startNode;
		List<IEdge> incomingEdges = null;
		while ((incomingEdges = graph.getIncomingEdges(current)).size() == 1) {
			visited.add(current);
			current = incomingEdges.get(0).getSource();
		}
		visited.add(current);
		return current;
	}

	private static <N extends INode> Set<String> collectChildrenUndirected(IDirectedGraph<N> graph, String startNode) {
		Set<String> children = new HashSet<>();
		collectChildrenUndirected(graph, startNode, children);
		return children;
	}

	private static <N extends INode> void collectLeafs(IDirectedGraph<N> graph, N startNode, Set<N> visited) {
		String walkThroughId = walkThroughOneWayPaths(graph, startNode.getId());
		List<IEdge> outgoingEdges = graph.getOutgoingEdges(walkThroughId);
		if (outgoingEdges.isEmpty()) {
			visited.add(graph.getNode(walkThroughId));
		} else {
			for (IEdge edge : outgoingEdges) {
				collectLeafs(graph, graph.getNode(edge.getTarget()), visited);
			}
		}
	}

	private static <N extends INode> String walkThroughOneWayPaths(IDirectedGraph<N> graph, String startNodeId) {
		String current = startNodeId;
		List<IEdge> outgoingEdges;
		while ((outgoingEdges = graph.getOutgoingEdges(current)).size() == 1) {
			current = outgoingEdges.get(0).getTarget();
		}
		return current;
	}

	private static <N extends INode> Set<N> collectLeafs(IDirectedGraph<N> graph, String startNode) {
		Set<N> leafs = new HashSet<>();
		collectLeafs(graph, graph.getNode(startNode), leafs);
		return leafs;
	}

	private static <N extends INode> boolean isCyclic(IDirectedGraph<N> graph) {
		String next = graph.getNodes().iterator().next().getId();
		return hasCycle(graph, next);
	}

	private static <N extends INode> boolean hasCycle(IDirectedGraph<N> graph, String startNode, Set<String> visited) {

		// try to search on one way path first
		String current = startNode;
		List<IEdge> outgoingEdges = graph.getOutgoingEdges(current);
		while ((outgoingEdges = graph.getOutgoingEdges(current)).size() == 1) {
			visited.add(current);
			IEdge edge = outgoingEdges.get(0);
			if (visited.contains(edge.getTarget())) {
				return true;
			}
			current = edge.getTarget();
		}

		visited.add(current);

		// explore
		for (IEdge edge : graph.getOutgoingEdges(current)) {
			if (visited.contains(edge.getTarget())) {
				return true;
			} else {
				return hasCycle(graph, edge.getTarget(), visited);
			}
		}
		return false;
	}

	private static <N extends INode> boolean hasCycle(IDirectedGraph<N> graph, String startNode) {
		return hasCycle(graph, startNode, new HashSet<>());
	}

	@Override
	public Collection<N> getNodes() {
		return graph.getNodes();
	}

	@Override
	public List<IEdge> getEdges() {
		return graph.getEdges();
	}

	@Override
	public boolean contains(String name) {
		return graph.contains(name);
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
	public boolean isRoot(N node) {
		return startNode.equals(node.getId());
	}

	@Override
	public Set<N> getLeafs() {
		if (leafs == null) {
			leafs = collectLeafs(graph, startNode);
		}
		return leafs;
	}

	@Override
	public boolean isLeaf(N node) {
		if (leafs == null) {
			leafs = collectLeafs(graph, startNode);
		}
		return leafs.contains(node);
	}

	@Override
	public String toString() {
		//@formatter:off
		var printTemplate = """
		%s {
		    Nodes [%s],
		    Edges [%s]
		}
		""";
		return String.format(printTemplate, this.getClass().getSimpleName(), 
				graph.getNodes().stream().map(this::printNode).collect(Collectors.joining(", ")),
				graph.getEdges().stream().map(this::printEdge).collect(Collectors.joining(", ")));
		//@formatter:on
	}

	private String printNode(N node) {
		return printNode(node.getId());
	}

	private String printNode(String nodeId) {
		if (nodeId.equals(startNode)) {
			return String.format("(%s)", nodeId);
		} else if (getLeafs().stream().anyMatch(leaf -> leaf.getId().equals(nodeId))) {
			return String.format("<%s>", nodeId);
		} else {
			return nodeId;
		}
	}

	private String printEdge(IEdge edge) {
		return String.format("{%s -> %s}", printNode(edge.getSource()), printNode(edge.getTarget()));
	}

	@Override
	public boolean contains(N node) {
		return graph.contains(node);
	}

	@Override
	public N getNode(String nodeId) {
		return graph.getNode(nodeId);
	}

	@Override
	public N getRoot() {
		return getNode(startNode);
	}

	@Override
	public boolean isLeaf(String nodeId) {
		if (leafs == null) {
			leafs = collectLeafs(graph, startNode);
		}
		return leafs.stream().anyMatch(l -> l.getId().equals(nodeId));
	}

	@Override
	public Collection<String> getNodeIds() {
		return graph.getNodeIds();
	}

}
