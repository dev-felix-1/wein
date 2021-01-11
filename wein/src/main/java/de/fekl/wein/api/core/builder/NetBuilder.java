package de.fekl.wein.api.core.builder;

import de.fekl.baut.AlphabeticalNames;
import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.core.NodeNames;
import de.fekl.dine.api.core.SimpleNode;
import de.fekl.dine.api.graph.DirectedGraphBuilder;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.dine.api.tree.SpongeNetBuilder;

public class NetBuilder {

	private String id;
	private NodesBuilder nodesBuilder;
	private EdgesBuilder edgesBuilder;

	private boolean withAlphabeticalNames = false;

	public NetBuilder nodes(NodesBuilder nodesBuilder) {
		this.nodesBuilder = nodesBuilder;
		return this;
	}

	public NetBuilder edges(EdgesBuilder edgesBuilder) {
		this.edgesBuilder = edgesBuilder;
		return this;
	}

	public NetBuilder id(String id) {
		this.id = id;
		return this;
	}

	public NetBuilder node(NodeBuilder nodeBuilder) {
		if (nodesBuilder == null) {
			nodesBuilder = new NodesBuilder();
		}
		nodesBuilder.node(nodeBuilder);
		return this;
	}

	public NetBuilder from(OutgoingEdgesBuilder outgoingEdgesBuilder) {
		if (edgesBuilder == null) {
			edgesBuilder = new EdgesBuilder();
		}
		edgesBuilder.outgoing(outgoingEdgesBuilder);
		return this;
	}

	public ISpongeNet buildNet() {
		SpongeNetBuilder spongeNetBuilder = new SpongeNetBuilder();
		DirectedGraphBuilder graphBuilder = new DirectedGraphBuilder();
		nodesBuilder.getNodeBuilders().forEach(nodeBuilder -> {
			buildNode(graphBuilder, nodeBuilder);
			if (nodeBuilder.getRole().equals("START")) {
				spongeNetBuilder.setStartNode(nodeBuilder.getId());
			}
		});
		if (edgesBuilder != null) {
			edgesBuilder.getOutgoing().forEach(out -> out.getTargets().forEach(target -> {
				String source = out.getSource();
				graphBuilder.addEdge(source, target);
			}));
		} else {
			nodesBuilder.getNodeBuilders().forEach(nodeBuilder -> nodeBuilder.getConnections().forEach(target -> {
				String source = nodeBuilder.getId();
				graphBuilder.addEdge(source, target);
			}));
		}
		spongeNetBuilder.setGraph(graphBuilder.build());
		return spongeNetBuilder.build();
	}

	public NetBuilder withAlphabeticalNames() {
		withAlphabeticalNames = true;
		return this;
	}

	private void buildNode(DirectedGraphBuilder simpleNet, NodeBuilder nodeBuilder) {
		String nodeId = nodeBuilder.getId();
		String role = nodeBuilder.getRole();
		INode impl = nodeBuilder.getImpl();
		if (nodeId == null) {
			if (withAlphabeticalNames) {
				nodeId = AlphabeticalNames.getNextLetter(ISpongeNet.class.getName() + simpleNet.hashCode());
			} else {
				nodeId = NodeNames.generateNodeName();
			}
			nodeBuilder.id(nodeId);
		}
		if (role == null) {
			role = "INTERMEDIATE";
			nodeBuilder.role(role);
		}
		if (impl == null) {
			impl = new SimpleNode(nodeId);
			nodeBuilder.impl(impl);
		}
		simpleNet.addNode(impl);
		nodeBuilder.getInlineNodes().forEach(inlineNodeBuilder -> {
			buildNode(simpleNet, inlineNodeBuilder);
			String source = nodeBuilder.getId();
			String target = inlineNodeBuilder.getId();
			simpleNet.addEdge(source, target);
		});

	}

}
