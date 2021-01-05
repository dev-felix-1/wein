package de.fekl.dine.api.core;

public class SimpleImmutableNet extends SimpleNet implements INet {

	public SimpleImmutableNet(INet net) {
		super(net.getId());
		super.addNode(net.getStartNodeId(), NodeRoles.START, net.getStartNode());
		net.getNodesByRole(NodeRoles.INTERMEDIATE).forEach((id, node) -> {
			super.addNode(id, NodeRoles.INTERMEDIATE, node);
		});
		net.getNodesByRole(NodeRoles.END).forEach((id, node) -> {
			super.addNode(id, NodeRoles.END, node);
		});
		
		net.getAllEdges().forEach(e->{
			super.addEdge(e.getSource(), e.getTarget());
		});
	}

	@Override
	public void addNode(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addNode(String id, String role) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void addNode(String id, String role, INodeDeprecated node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void addEdge(String srcNodeId, String targetNodeId) {
		throw new UnsupportedOperationException();
	}

}
