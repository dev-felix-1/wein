package de.fekl.dine.api.tree;

import java.util.Collection;
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
		if (!isConnected(graph)) {
			throw new IllegalArgumentException("graph is not fully connected :\n" + graph.toString());
		}
		if (isCyclic(graph)) {
			throw new IllegalArgumentException("graph is cyclic");
		}
		this.graph = graph;
		this.startNode = startNode;
	}

	private static <N extends INode> boolean isConnected(IDirectedGraph<N> graph) {
		N next = graph.getNodes().iterator().next();
		Set<String> collectChildren = collectChildrenUndirected(graph, next.getId());
		return collectChildren.size() == graph.getNodes().size();
	}

	private static <N extends INode> void collectChildrenUndirected(IDirectedGraph<N> graph, String startNode,
			Set<String> visited) {
		visited.add(startNode);
		for (IEdge edge : graph.getOutgoingEdges(startNode)) {
			if (!visited.contains(edge.getTarget())) {
				collectChildrenUndirected(graph, edge.getTarget(), visited);
			}
		}
		for (IEdge edge : graph.getIncomingEdges(startNode)) {
			if (!visited.contains(edge.getSource())) {
				collectChildrenUndirected(graph, edge.getSource(), visited);
			}
		}
	}

	private static <N extends INode> Set<String> collectChildrenUndirected(IDirectedGraph<N> graph, String startNode) {
		Set<String> children = new HashSet<>();
		collectChildrenUndirected(graph, startNode, children);
		return children;
	}

	private static <N extends INode> void collectLeafs(IDirectedGraph<N> graph, N startNode, Set<N> visited) {
		List<IEdge> outgoingEdges = graph.getOutgoingEdges(startNode.getId());
		if (outgoingEdges.isEmpty()) {
			visited.add(startNode);
		} else {
			for (IEdge edge : outgoingEdges) {
				collectLeafs(graph, graph.getNode(edge.getTarget()), visited);
			}
		}
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

}
