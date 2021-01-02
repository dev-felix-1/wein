package de.fekl.dine.api.core;

import java.util.List;
import java.util.Map;

public interface INet {

	String getId();

	String print();

	void addNode(String id, String role, INode node);

	void addNode(String id, String role);

	void addNode(String id);

	void addEdge(String srcNodeId, String targetNodeId);

	INode getNode(String id);
	
	void setNode(String id, String role, INode node);
	
	void setStartNode(INode node);

	INode getStartNode();

	String getStartNodeId();

	Map<String, INode> getAllNodes();

	Map<String, INode> getNodesByRole(String role);

	List<IEdge> getAllEdges();
	
	List<IEdge> getOutgoingEdges(String srcNodeId);
	
	List<IEdge> getIncomingEdges(String targetNodeId);

	boolean containsNode(String id);

}
