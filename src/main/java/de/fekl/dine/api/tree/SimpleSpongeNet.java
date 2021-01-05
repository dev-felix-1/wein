package de.fekl.dine.api.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.baut.Precondition;
import de.fekl.dine.api.core.IEdge;
import de.fekl.dine.api.graph.IDirectedGraph;
import de.fekl.dine.api.graph.INode;

public class SimpleSpongeNet implements ISpongeNet {

	private final IDirectedGraph graph;
	private final String startNode;

	public SimpleSpongeNet(IDirectedGraph graph, String startNode) {
		Precondition.isNotNull(graph);
		Precondition.isNotEmpty(startNode);
		if (!graph.contains(startNode)) {
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
		INode next = graph.getNodes().iterator().next();
		Set<String> collectChildren = collectChildrenUndirected(graph, next.getId());
		return collectChildren.size() == graph.getNodes().size();
	}

	private static void collectChildrenUndirected(IDirectedGraph graph, String startNode, Set<String> visited) {
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

	private static Set<String> collectChildrenUndirected(IDirectedGraph graph, String startNode) {
		Set<String> children = new HashSet<>();
		collectChildrenUndirected(graph, startNode, children);
		return children;
	}

	private static void collectLeafs(IDirectedGraph graph, INode startNode, Set<INode> visited) {
		List<IEdge> outgoingEdges = graph.getOutgoingEdges(startNode.getId());
		if (outgoingEdges.isEmpty()) {
			visited.add(startNode);
		} else {
			for (IEdge edge : outgoingEdges) {
				collectLeafs(graph, graph.getNode(edge.getTarget()), visited);
			}
		}
	}

	private static Set<INode> collectLeafs(IDirectedGraph graph, String startNode) {
		Set<INode> leafs = new HashSet<>();
		collectLeafs(graph, graph.getNode(startNode), leafs);
		return leafs;
	}

	private static boolean isCyclic(IDirectedGraph graph) {
		String next = graph.getNodes().iterator().next().getId();
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
	public Collection<INode> getNodes() {
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
	public boolean isRoot(INode node) {
		return startNode.equals(node.getId());
	}

	@Override
	public Set<INode> getLeafs() {
		return collectLeafs(graph, startNode);
	}

	@Override
	public boolean isLeaf(INode node) {
		return collectLeafs(graph, startNode).contains(node);
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

	private String printNode(INode node) {
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
	public boolean contains(INode node) {
		return graph.contains(node);
	}

	@Override
	public INode getNode(String nodeId) {
		return graph.getNode(nodeId);
	}

	@Override
	public INode getRoot() {
		return getNode(startNode);
	}

}
