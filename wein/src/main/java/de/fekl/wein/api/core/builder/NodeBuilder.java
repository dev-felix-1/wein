package de.fekl.wein.api.core.builder;

import java.util.ArrayList;
import java.util.List;

import de.fekl.dine.api.core.INode;

@Deprecated
public class NodeBuilder {

	private String id;
	private String role;
	private INode impl;
	private List<String> connections = new ArrayList<>();
	private List<NodeBuilder> inlineNodes = new ArrayList<>();

	public String getId() {
		return id;
	}

	public NodeBuilder id(String id) {
		this.id = id;
		return this;
	}

	public String getRole() {
		return role;
	}

	public NodeBuilder role(String role) {
		this.role = role;
		return this;
	}

	public INode getImpl() {
		return impl;
	}

	public NodeBuilder impl(INode impl) {
		this.impl = impl;
		return this;
	}

	public NodeBuilder to(String target) {
		this.connections.add(target);
		return this;
	}
	
	public NodeBuilder to(NodeBuilder target) {
		this.inlineNodes.add(target);
		return this;
	}
	
	public List<String> getConnections() {
		return connections;
	}
	
	List<NodeBuilder> getInlineNodes() {
		return inlineNodes;
	}

}
