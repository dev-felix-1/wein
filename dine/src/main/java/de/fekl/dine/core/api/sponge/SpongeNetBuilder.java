package de.fekl.dine.core.api.sponge;

import java.util.Iterator;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.graph.IDirectedGraph;
import de.fekl.dine.core.api.node.INode;
import de.fekl.dine.core.impl.sponge.SimpleSpongeNetFactory;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <N>
 */
public class SpongeNetBuilder<N extends INode> {

	private IDirectedGraph<N> graph;
	private String startNode;
	private ISpongeNetFactory<N> spongeNetFactory = new SimpleSpongeNetFactory<>();

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
		if (startNode == null) {
			Iterator<N> iterator = graph.getNodes().iterator();
			N current = null;
			while (iterator.hasNext() && graph.getIncomingEdges(current = iterator.next()).size() > 0)
				;
			startNode = current.getId();
		}
		return spongeNetFactory.createSpongeNet(startNode, graph);
	}

}
