package de.fekl.dine.api.graph;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.edge.IEdgeBuilder;
import de.fekl.dine.api.edge.IEdgeFactory;
import de.fekl.dine.api.edge.SimpleEdgeBuilder;
import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.INodeBuilder;
import de.fekl.dine.api.node.INodeFactory;
import de.fekl.dine.api.node.SimpleNodeBuilder;

public class DirectedGraphBuilder<N extends INode> {

	public static final boolean LOOK_UP_NODES_FROM_EDGES = true;

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

	private final Set<N> nodes = new HashSet<>();
	private final List<IEdge> edges = new ArrayList<>();

	private final List<String> nodesToCreateByName = new ArrayList<>();
	private final List<Entry<String, String>> edgesToCreate = new ArrayList<>();

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

	public IDirectedGraph<N> build() {
		for (String nodeId : nodesToCreateByName) {
			if (nodeBuilderHolder == null || nodeBuilderHolder.nodeBuilder == null) {
				throw new IllegalStateException("You have to set a nodeBuilder, when you are using #addNode(String)");
			}
			N node = nodeBuilderHolder.nodeBuilder.id(nodeId).build();
			nodes.add(node);
		}
		for (Entry<String, String> entry : edgesToCreate) {
			if (edgeBuilderHolder == null || edgeBuilderHolder.edgeBuilder == null) {
				throw new IllegalStateException(
						"You have to set an edgeBuilder, when you are using #addEdge(String,String)");
			}
			IEdge edge = edgeBuilderHolder.edgeBuilder.source(entry.getKey()).target(entry.getValue()).build();
			edges.add(edge);
		}
		if (LOOK_UP_NODES_FROM_EDGES) {
			for (IEdge edge : edges) {
				List<String> nodeNames = nodes.stream().map(n -> n.getId()).collect(Collectors.toList());
				List<String> addedNodeNames = new ArrayList<>();
				if (nodeBuilderHolder != null && nodeBuilderHolder.nodeBuilder != null) {
					if (!nodeNames.contains(edge.getSource()) && !addedNodeNames.contains(edge.getSource())) {
						N node = nodeBuilderHolder.nodeBuilder.id(edge.getSource()).build();
						nodes.add(node);
					}
					if (!nodeNames.contains(edge.getTarget()) && !addedNodeNames.contains(edge.getTarget())) {
						N node = nodeBuilderHolder.nodeBuilder.id(edge.getTarget()).build();
						nodes.add(node);
					}
				}
			}
		}
		return directedGraphFactory.createDirectedGraph(nodes, edges);
	}

}
