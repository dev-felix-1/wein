package de.fekl.dine.core.api.graph;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.util.AbstractIdHolder;
import de.fekl.dine.util.Precondition;

/**
 * Abstract implementation for a graph that has nodes. Edges are not defined
 * yet.
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifices the kind of nodes the graph holdes
 * 
 */
public abstract class AbstractGraph<N extends INode> extends AbstractIdHolder<String> implements IGraph<N> {

	public static volatile boolean ALLOW_EMPTY_NODE_SET = false;

	private final Map<String, N> nodeNamesMap;

	public AbstractGraph(Set<N> nodes) {
		super(GraphNames.generateName());
		if (!ALLOW_EMPTY_NODE_SET) {
			Precondition.isNotEmpty(nodes);
		}
		Map<String, N> nodeNamesMap = nodes.stream().collect(Collectors.toUnmodifiableMap(n -> n.getId(), n -> n));
		this.nodeNamesMap = nodeNamesMap;
	}

	public AbstractGraph(String id, Set<N> nodes) {
		super(id);
		if (!ALLOW_EMPTY_NODE_SET) {
			Precondition.isNotEmpty(nodes);
		}
		Map<String, N> nodeNamesMap = nodes.stream().collect(Collectors.toUnmodifiableMap(n -> n.getId(), n -> n));
		this.nodeNamesMap = nodeNamesMap;
	}

	@Override
	public Collection<N> getNodes() {
		return nodeNamesMap.values();
	}

	@Override
	public Collection<String> getNodeIds() {
		return nodeNamesMap.keySet();
	}

	@Override
	public boolean contains(String nodeId) {
		return nodeNamesMap.containsKey(nodeId);
	}

	@Override
	public boolean contains(N node) {
		return nodeNamesMap.containsValue(node);
	}

	@Override
	public N getNode(String nodeId) {
		return nodeNamesMap.get(nodeId);
	}

	/**
	 * 
	 * @return an internal map of (nodeId) -> (nodeObject)
	 */
	protected Map<String, N> getNodeNamesMap() {
		return nodeNamesMap;
	}

}
