package de.fekl.dine.core.api.graph;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.edge.IEdgeBuilder;
import de.fekl.dine.core.api.edge.IEdgeFactory;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.node.INodeBuilder;
import de.fekl.dine.core.api.node.INodeFactory;
import de.fekl.dine.core.impl.edge.SimpleEdgeBuilder;
import de.fekl.dine.core.impl.graph.SimpleDirectedGraphFactory;
import de.fekl.dine.core.impl.node.SimpleNodeBuilder;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifies the kind of nodes the graph holdes
 */
public class DirectedGraphBuilder<N extends INode> {

	public static final boolean LOOK_UP_NODES_FROM_EDGES = true;
	public static final boolean ALLOW_DUPLICATE_EDGES = false;

	private class NodeBuilderHolder<F extends INodeFactory<N>, B extends INodeBuilder<N, F, B>> {
		private final INodeBuilder<N, F, B> nodeBuilder;

		NodeBuilderHolder(INodeBuilder<N, F, B> nodeBuilder) {
			this.nodeBuilder = nodeBuilder;
		}
	}

	private class EdgeBuilderHolder<E extends IEdge, F extends IEdgeFactory<E>, B extends IEdgeBuilder<E, F, B>> {
		private final IEdgeBuilder<E, F, B> edgeBuilder;

		EdgeBuilderHolder(IEdgeBuilder<E, F, B> edgeBuilder) {
			this.edgeBuilder = edgeBuilder;
		}
	}

	private final Set<N> nodes = new TreeSet<>((a, b) -> a.getId().compareTo(b.getId()));
	private final List<IEdge> edges = new ArrayList<>();

	private final List<String> nodesToCreateByName = new ArrayList<>();
	private final List<Entry<Object, Object>> edgesToCreate = new ArrayList<>();

	private IDirectedGraphFactory<N> directedGraphFactory = new SimpleDirectedGraphFactory<N>();
	private EdgeBuilderHolder<?, ?, ?> edgeBuilderHolder = new EdgeBuilderHolder<>(new SimpleEdgeBuilder());
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private NodeBuilderHolder<?, ?> nodeBuilderHolder = new NodeBuilderHolder(new SimpleNodeBuilder());

	public DirectedGraphBuilder<N> setGraphFactory(IDirectedGraphFactory<N> factory) {
		directedGraphFactory = factory;
		return this;
	}

	public <E extends IEdge, F extends IEdgeFactory<E>, B extends IEdgeBuilder<E, F, B>> DirectedGraphBuilder<N> setEdgeBuilder(
			B edgeBuilder) {
		edgeBuilderHolder = new EdgeBuilderHolder<>(edgeBuilder);
		return this;
	}

	public <F extends INodeFactory<N>, B extends INodeBuilder<N, F, B>> DirectedGraphBuilder<N> setNodeBuilder(
			B nodeBuilder) {
		nodeBuilderHolder = new NodeBuilderHolder<>(nodeBuilder);
		return this;
	}

	public DirectedGraphBuilder<N> addNode(String nodeId) {
		nodesToCreateByName.add(nodeId);
		return this;
	}

	public DirectedGraphBuilder<N> addNode(N node) {
		nodes.add(node);
		return this;
	}

	public <F extends INodeFactory<N>, B extends INodeBuilder<N, F, B>> DirectedGraphBuilder<N> addNode(B nodeBuilder) {
		nodes.add(nodeBuilder.build());
		return this;
	}

	public DirectedGraphBuilder<N> addEdge(IEdge edge) {
		edges.add(edge);
		return this;
	}

	public DirectedGraphBuilder<N> addEdge(String sourceNode, String targetNode) {
		edgesToCreate.add(new AbstractMap.SimpleEntry<>(sourceNode, targetNode));
		return this;
	}

	public DirectedGraphBuilder<N> addEdge(String sourceNode, N targetNode) {
		edgesToCreate.add(new AbstractMap.SimpleEntry<>(sourceNode, targetNode));
		return this;
	}

	public DirectedGraphBuilder<N> addEdge(N sourceNode, String targetNode) {
		edgesToCreate.add(new AbstractMap.SimpleEntry<>(sourceNode, targetNode));
		return this;
	}

	public DirectedGraphBuilder<N> addEdge(N sourceNode, N targetNode) {
		edgesToCreate.add(new AbstractMap.SimpleEntry<>(sourceNode, targetNode));
		return this;
	}

	public DirectedGraphBuilder<N> chain(N... nodes) {
		return chain((Object[]) nodes);
	}

	public DirectedGraphBuilder<N> chain(String... nodes) {
		return chain((Object[]) nodes);
	}

	private DirectedGraphBuilder<N> chain(Object[] nodes) {
		if (nodes.length < 2) {
			throw new IllegalArgumentException("Cannot make a chain from less than two objects");
		}
		for (int i = 0; i < nodes.length - 1; i++) {
			edgesToCreate.add(new AbstractMap.SimpleEntry<>(nodes[i], nodes[i + 1]));
		}
		return this;
	}

	public DirectedGraphBuilder<N> fork(N sourceNode, N... nodes) {
		return fork(sourceNode, (Object[]) nodes);
	}

	public DirectedGraphBuilder<N> fork(N sourceNode, String... nodes) {
		return fork(sourceNode, (Object[]) nodes);
	}

	public DirectedGraphBuilder<N> fork(String sourceNode, String... nodes) {
		return fork(sourceNode, (Object[]) nodes);
	}

	public DirectedGraphBuilder<N> fork(String sourceNode, N... nodes) {
		return fork(sourceNode, (Object[]) nodes);
	}

	private DirectedGraphBuilder<N> fork(Object sourceNode, Object[] nodes) {
		if (nodes.length < 1) {
			throw new IllegalArgumentException("Cannot make a fork to less than one object");
		}
		for (int i = 0; i < nodes.length; i++) {
			edgesToCreate.add(new AbstractMap.SimpleEntry<>(sourceNode, nodes[i]));
		}
		return this;
	}

	public class DirectedGraphBuilderJoinMediator<B> {
		private Object[] nodes;

		protected DirectedGraphBuilderJoinMediator(Object[] nodes) {
			this.nodes = nodes;
		}

		public B on(N targetNode) {
			return join(nodes, targetNode);
		}

		public B on(String targetNode) {
			return join(nodes, targetNode);
		}

		protected B join(Object[] nodes, Object targetNode) {
			return (B) DirectedGraphBuilder.this.join(nodes, targetNode);
		}
	}

	public DirectedGraphBuilderJoinMediator<DirectedGraphBuilder<N>> join(N... nodes) {
		return new DirectedGraphBuilderJoinMediator<>(nodes);
	}

	public DirectedGraphBuilderJoinMediator<DirectedGraphBuilder<N>> join(String... nodes) {
		return new DirectedGraphBuilderJoinMediator<>(nodes);
	}

	protected DirectedGraphBuilder<N> join(Object[] nodes, Object targetNode) {
		if (nodes.length < 1) {
			throw new IllegalArgumentException("Cannot make a join from nothing");
		}
		for (int i = 0; i < nodes.length; i++) {
			edgesToCreate.add(new AbstractMap.SimpleEntry<>(nodes[i], targetNode));
		}
		return this;
	}

	/**
	 * 
	 * @return a directed graph implementation
	 */
	@SuppressWarnings("unchecked")
	public IDirectedGraph<N> build() {
		for (String nodeId : nodesToCreateByName) {
			if (nodeBuilderHolder == null || nodeBuilderHolder.nodeBuilder == null) {
				throw new IllegalStateException("You have to set a nodeBuilder, when you are using #addNode(String)");
			}
			N node = nodeBuilderHolder.nodeBuilder.id(nodeId).build();
			nodes.add(node);
		}
		for (Entry<Object, Object> entry : edgesToCreate) {
			if (edgeBuilderHolder == null || edgeBuilderHolder.edgeBuilder == null) {
				throw new IllegalStateException(
						"You have to set an edgeBuilder, when you are using #addEdge(String,String)");
			}
			Object key = entry.getKey();
			Object value = entry.getValue();
			String sourceId;
			String targetId;

			if (key instanceof INode) {
				sourceId = ((INode) key).getId();
				nodes.add((N) key);
			} else {
				sourceId = (String) key;
			}
			if (value instanceof INode) {
				targetId = ((INode) value).getId();
				nodes.add((N) value);
			} else {
				targetId = (String) value;
			}

			IEdge edge = edgeBuilderHolder.edgeBuilder.source(sourceId).target(targetId).build();
			edges.add(edge);
		}
		if (LOOK_UP_NODES_FROM_EDGES) {
			for (IEdge edge : edges) {
				List<String> nodeNames = nodes.stream().map(n -> n.getId()).collect(Collectors.toList());
				List<String> addedNodeNames = new ArrayList<>(edges.size() / 2);
				if (nodeBuilderHolder != null && nodeBuilderHolder.nodeBuilder != null) {
					if (!nodeNames.contains(edge.getSource()) && !addedNodeNames.contains(edge.getSource())) {
						N node = nodeBuilderHolder.nodeBuilder.id(edge.getSource()).build();
						nodes.add(node);
						addedNodeNames.add(edge.getSource());
					}
					if (!nodeNames.contains(edge.getTarget()) && !addedNodeNames.contains(edge.getTarget())) {
						N node = nodeBuilderHolder.nodeBuilder.id(edge.getTarget()).build();
						nodes.add(node);
						addedNodeNames.add(edge.getTarget());
					}
				}
			}
		}
		if (ALLOW_DUPLICATE_EDGES) {
			return directedGraphFactory.createDirectedGraph(nodes, edges);
		} else {
			return directedGraphFactory.createDirectedGraph(nodes, new ArrayList<>(new HashSet<>(edges)));
		}
	}

}
