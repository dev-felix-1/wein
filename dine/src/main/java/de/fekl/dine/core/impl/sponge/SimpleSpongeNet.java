package de.fekl.dine.core.impl.sponge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.impl.edge.SimpleEdge;
import de.fekl.dine.util.Precondition;

//FIXME it is not clear, if we really want to support only one start node. For BPMN, this would a problem, because there might be joining events.
//also we cannot detect if everything is connected correctly
public class SimpleSpongeNet<N extends INode> implements ISpongeNet<N> {

	private final IDirectedGraph<N> graph;
	private final String startNode;

	// caching
	private Set<N> leafs;
	private Map<IEdge, List<List<IEdge>>> paths = new HashMap<>();

	public SimpleSpongeNet(IDirectedGraph<N> graph, String startNode) {
		Precondition.isNotNull(graph, "Parameter %s is null", "graph");
		Precondition.isNotEmpty(startNode, "Parameter %s is null", "startNode");
		if (!graph.contains(startNode)) {
			throw new IllegalArgumentException("startNode is not part of the graph");
		}
		checkIsConnected(graph, startNode);
		if (hasCycle(graph, startNode)) {
			throw new IllegalArgumentException("graph is cyclic");
		}
		this.graph = graph;
		this.startNode = startNode;
	}

	private static <N extends INode> void checkIsConnected(IDirectedGraph<N> graph, String startNode) {
		Set<String> collectChildren = collectChildren(graph, startNode);
		if (collectChildren.size() != graph.getNodes().size()) {
			ArrayList<String> nodeIds = new ArrayList<>(graph.getNodeIds());
			nodeIds.removeAll(collectChildren);
			throw new IllegalArgumentException(
					String.format("Found %s unconnected nodes: %s %ngraph>%s", nodeIds.size(), nodeIds, graph));
		}
	}

	private static <N extends INode> void collectChildren(IDirectedGraph<N> graph, String startNode,
			Set<String> visited) {
		String currentOut = collectChildrenOnewayOutgoingPath(graph, startNode, visited);
		for (IEdge edge : graph.getOutgoingEdges(currentOut)) {
			if (!visited.contains(edge.getTarget())) {
				collectChildren(graph, edge.getTarget(), visited);
			}
		}
	}

	private static <N extends INode> String collectChildrenOnewayOutgoingPath(IDirectedGraph<N> graph, String startNode,
			Set<String> visited) {
		String current = startNode;
		List<IEdge> outgoingEdges = null;
		while ((outgoingEdges = graph.getOutgoingEdges(current)).size() == 1) {
			if (visited.contains(current)) {
				return current;
			}
			visited.add(current);
			current = outgoingEdges.get(0).getTarget();
		}
		visited.add(current);
		return current;
	}

	private static <N extends INode> Set<String> collectChildren(IDirectedGraph<N> graph, String startNode) {
		Set<String> children = new HashSet<>();
		collectChildren(graph, startNode, children);
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

	private static boolean isLoopingEdge(IEdge edge) {
		return edge.getSource().equals(edge.getTarget());
	}

	private static boolean edgeLeadsToCycle(Set<String> ancestors, IEdge edge) {
		return isLoopingEdge(edge) || ancestors.contains(edge.getTarget());
	}

	private static <N extends INode> boolean hasCycle(IDirectedGraph<N> graph, String startNode,
			Set<String> ancestors) {

		String current = startNode;

		// try to search on one way path first - avoid recursion for stackoverflow
		// issues in very big graphs
		List<IEdge> outgoingEdges;
		while ((outgoingEdges = graph.getOutgoingEdges(current)).size() == 1) {
			ancestors.add(current);
			IEdge edge = outgoingEdges.get(0);
			if (edgeLeadsToCycle(ancestors, edge)) {
				return true;
			}
			current = edge.getTarget();
		}

		ancestors.add(current);

		// explore
		for (IEdge edge : graph.getOutgoingEdges(current)) {
			if (edgeLeadsToCycle(ancestors, edge)) {
				return true;
			} else {
				if (hasCycle(graph, edge.getTarget(), new HashSet<>(ancestors))) {
					return true;
				}
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
		return isRoot(node.getId());
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
	public List<List<IEdge>> getPaths(String sourceNodeId, String targetNodeId) {
		SimpleEdge pathId = new SimpleEdge(sourceNodeId, targetNodeId);
		return paths.computeIfAbsent(pathId, p -> getPaths(graph, sourceNodeId, targetNodeId, new ArrayList<>()));
	}

	protected static List<List<IEdge>> getPaths(IDirectedGraph<?> graph, String sourceNodeId, String targetNodeId,
			List<IEdge> currentTrack) {
		List<List<IEdge>> resultPaths = new ArrayList<List<IEdge>>();
		List<IEdge> incoming;
		String nextNodeId = targetNodeId;
		List<IEdge> nextTrack = new ArrayList<>(currentTrack);

		while ((incoming = graph.getIncomingEdges(nextNodeId)).size() == 1) {
			IEdge edge = incoming.get(0);
			nextTrack.add(edge);
			nextNodeId = edge.getSource();
			if (nextNodeId.equals(sourceNodeId)) {
				Collections.reverse(nextTrack);
				resultPaths.add(nextTrack);
				return resultPaths;
			}
		}

		for (IEdge edge : incoming) {
			List<IEdge> nextTrackSplit = new ArrayList<>(nextTrack);
			nextTrackSplit.add(edge);
			if (edge.getSource().equals(nextNodeId)) {
				Collections.reverse(nextTrackSplit);
				resultPaths.add(nextTrackSplit);
			} else {
				resultPaths.addAll(getPaths(graph, sourceNodeId, edge.getSource(), nextTrackSplit));
			}
		}
		return resultPaths;
	}

	public Collection<String> getNodeIds() {
		return graph.getNodeIds();
	}

	@Override
	public boolean isRoot(String nodeId) {
		return startNode.equals(nodeId);
	}

	@Override
	public Set<String> getLeafIds() {
		return getLeafs().stream().map(INode::getId).collect(Collectors.toSet());
	}

	@Override
	public String getId() {
		return graph.getId();
	}

}
