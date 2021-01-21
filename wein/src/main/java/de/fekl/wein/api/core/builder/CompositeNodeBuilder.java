package de.fekl.wein.api.core.builder;

import java.util.ArrayList;
import java.util.List;

import de.fekl.dine.core.api.node.INode;

public class CompositeNodeBuilder {

	private String id;
	private String role;
	private INode impl;
	private List<String> connections = new ArrayList<>();
	private List<CompositeNodeBuilder> inlineNodes = new ArrayList<>();

	public String getId() {
		return id;
	}

	public CompositeNodeBuilder id(String id) {
		this.id = id;
		return this;
	}

	public String getRole() {
		return role;
	}

	public CompositeNodeBuilder role(String role) {
		this.role = role;
		return this;
	}

	public INode getImpl() {
		return impl;
	}

	public CompositeNodeBuilder impl(INode impl) {
		this.impl = impl;
		return this;
	}

	public CompositeNodeBuilder to(String target) {
		this.connections.add(target);
		return this;
	}
	
	public CompositeNodeBuilder to(CompositeNodeBuilder target) {
		this.inlineNodes.add(target);
		return this;
	}
	
	public List<String> getConnections() {
		return connections;
	}
	
	List<CompositeNodeBuilder> getInlineNodes() {
		return inlineNodes;
	}

}
