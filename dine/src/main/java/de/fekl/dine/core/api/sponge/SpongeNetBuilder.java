package de.fekl.dine.core.api.sponge;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.graph.DirectedGraphBuilder.DirectedGraphBuilderJoinMediator;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.api.node.INodeBuilder;
import de.fekl.dine.core.api.node.INodeFactory;
import de.fekl.dine.core.impl.sponge.SimpleSpongeNetFactory;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N> specifies the kind of nodes the graph holdes
 */
public class SpongeNetBuilder<N extends INode> {

	private IDirectedGraph<N> graph;
	private String startNode;
	private ISpongeNetFactory<N> spongeNetFactory = new SimpleSpongeNetFactory<>();

	private DirectedGraphBuilder<N> directedGraphBuilderDelegate = new DirectedGraphBuilder<>();

	public SpongeNetBuilder() {
		super();
	}

	public SpongeNetBuilder(DirectedGraphBuilder<N> builder) {
		this();
		setGraph(builder);
	}

	public SpongeNetBuilder<N> setSpongeNetFactory(ISpongeNetFactory<N> factory) {
		spongeNetFactory = factory;
		return this;
	}

	public SpongeNetBuilder<N> setGraph(IDirectedGraph<N> graph) {
		this.graph = graph;
		return this;
	}

	public SpongeNetBuilder<N> setGraph(DirectedGraphBuilder<N> builder) {
		this.graph = builder.build();
		return this;
	}

	public SpongeNetBuilder<N> setStartNode(String startNode) {
		this.startNode = startNode;
		return this;
	}

	public ISpongeNet<N> build() {
		if (graph == null) {
			graph = directedGraphBuilderDelegate.build();
		}
		if (startNode == null) {
			Iterator<N> iterator = graph.getNodes().iterator();
			N current = null;
			while (iterator.hasNext() && graph.getIncomingEdges(current = iterator.next()).size() > 0)
				;
			startNode = current.getId();
		}
		return spongeNetFactory.createSpongeNet(startNode, graph);
	}

	// Delegate Methods
	public SpongeNetBuilder<N> addNode(String nodeId) {
		directedGraphBuilderDelegate.addNode(nodeId);
		return this;
	}

	public SpongeNetBuilder<N> addNode(N node) {
		directedGraphBuilderDelegate.addNode(node);
		return this;
	}

	public <F extends INodeFactory<N>, B extends INodeBuilder<N, F, B>> SpongeNetBuilder<N> addNode(B nodeBuilder) {
		directedGraphBuilderDelegate.addNode(nodeBuilder);
		return this;
	}

	public SpongeNetBuilder<N> addEdge(IEdge edge) {
		directedGraphBuilderDelegate.addEdge(edge);
		return this;
	}

	public SpongeNetBuilder<N> addEdge(String sourceNode, String targetNode) {
		directedGraphBuilderDelegate.addEdge(sourceNode, targetNode);
		return this;
	}

	public SpongeNetBuilder<N> addEdge(String sourceNode, N targetNode) {
		directedGraphBuilderDelegate.addEdge(sourceNode, targetNode);
		return this;
	}

	public SpongeNetBuilder<N> addEdge(N sourceNode, String targetNode) {
		directedGraphBuilderDelegate.addEdge(sourceNode, targetNode);
		return this;
	}

	public SpongeNetBuilder<N> addEdge(N sourceNode, N targetNode) {
		directedGraphBuilderDelegate.addEdge(sourceNode, targetNode);
		return this;
	}

	public SpongeNetBuilder<N> chain(N... nodes) {
		directedGraphBuilderDelegate.chain(nodes);
		return this;
	}

	public SpongeNetBuilder<N> chain(String... nodes) {
		directedGraphBuilderDelegate.chain(nodes);
		return this;
	}

	public SpongeNetBuilder<N> fork(N sourceNode, N... nodes) {
		directedGraphBuilderDelegate.fork(sourceNode, nodes);
		return this;
	}

	public SpongeNetBuilder<N> fork(N sourceNode, String... nodes) {
		directedGraphBuilderDelegate.fork(sourceNode, nodes);
		return this;
	}

	public SpongeNetBuilder<N> fork(String sourceNode, String... nodes) {
		directedGraphBuilderDelegate.fork(sourceNode, nodes);
		return this;
	}

	public SpongeNetBuilder<N> fork(String sourceNode, N... nodes) {
		directedGraphBuilderDelegate.fork(sourceNode, nodes);
		return this;
	}

	public class SpongeNetBuilderJoinMediator
			extends DirectedGraphBuilder<N>.DirectedGraphBuilderJoinMediator<SpongeNetBuilder<N>> {
		protected SpongeNetBuilderJoinMediator(DirectedGraphBuilder<N> directedGraphBuilder, Object[] nodes) {
			directedGraphBuilder.super(nodes);
		}

		@Override
		public SpongeNetBuilder<N> on(N targetNode) {
			super.on(targetNode);
			return SpongeNetBuilder.this;
		}

		@Override
		public SpongeNetBuilder<N> on(String targetNode) {
			super.on(targetNode);
			return SpongeNetBuilder.this;
		}
	}

	public SpongeNetBuilderJoinMediator join(N... nodes) {
		return new SpongeNetBuilderJoinMediator(directedGraphBuilderDelegate, (Object[]) nodes);
	}

	public SpongeNetBuilderJoinMediator join(String... nodes) {
		return new SpongeNetBuilderJoinMediator(directedGraphBuilderDelegate, (Object[]) nodes);
	}

}
