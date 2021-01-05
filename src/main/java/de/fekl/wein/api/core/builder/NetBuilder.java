package de.fekl.wein.api.core.builder;

import de.fekl.baut.AlphabeticalNames;
import de.fekl.dine.api.core.INet;
import de.fekl.dine.api.core.INodeDeprecated;
import de.fekl.dine.api.core.NodeNames;
import de.fekl.dine.api.core.NodeRoles;
import de.fekl.dine.api.core.SimpleNet;
import de.fekl.dine.api.core.SimpleNodeDeprecated;

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

	public INet buildNet() {
		SimpleNet simpleNet = new SimpleNet(id);
		nodesBuilder.getNodeBuilders().forEach(nodeBuilder -> buildNode(simpleNet, nodeBuilder));
		if (edgesBuilder != null) {
			edgesBuilder.getOutgoing().forEach(out -> out.getTargets().forEach(target -> {
				String source = out.getSource();
				simpleNet.addEdge(source, target);
			}));
		} else {
			nodesBuilder.getNodeBuilders().forEach(nodeBuilder -> nodeBuilder.getConnections().forEach(target -> {
				String source = nodeBuilder.getId();
				simpleNet.addEdge(source, target);
			}));
		}
		return simpleNet;
	}

	public NetBuilder withAlphabeticalNames() {
		withAlphabeticalNames = true;
		return this;
	}

	private void buildNode(SimpleNet simpleNet, NodeBuilder nodeBuilder) {
		String nodeId = nodeBuilder.getId();
		String role = nodeBuilder.getRole();
		INodeDeprecated impl = nodeBuilder.getImpl();
		if (nodeId == null) {
			if (withAlphabeticalNames) {
				nodeId = AlphabeticalNames.getNextLetter(SimpleNet.class.getName() + simpleNet.getId());
			} else {
				nodeId = NodeNames.generateNodeName();
			}
			nodeBuilder.id(nodeId);
		}
		if (role == null) {
			role = NodeRoles.INTERMEDIATE;
			nodeBuilder.role(role);
		}
		if (impl == null) {
			impl = new SimpleNodeDeprecated();
			nodeBuilder.impl(impl);
		}
		simpleNet.addNode(nodeId, role, impl);
		nodeBuilder.getInlineNodes().forEach(inlineNodeBuilder -> {
			buildNode(simpleNet, inlineNodeBuilder);
			String source = nodeBuilder.getId();
			String target = inlineNodeBuilder.getId();
			simpleNet.addEdge(source, target);
		});

	}

}
