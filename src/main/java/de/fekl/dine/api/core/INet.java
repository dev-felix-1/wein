package de.fekl.dine.api.core;

import java.util.List;
import java.util.Map;
@Deprecated
public interface INet {

	String getId();

	String print();

	void addNode(String id, String role, INodeDeprecated node);

	void addNode(String id, String role);

	void addNode(String id);

	void addEdge(String srcNodeId, String targetNodeId);

	INodeDeprecated getNode(String id);
	
	void setNode(String id, String role, INodeDeprecated node);
	
	void setStartNode(INodeDeprecated node);

	INodeDeprecated getStartNode();

	String getStartNodeId();

	Map<String, INodeDeprecated> getAllNodes();

	Map<String, INodeDeprecated> getNodesByRole(String role);

	List<IEdge> getAllEdges();
	
	List<IEdge> getOutgoingEdges(String srcNodeId);
	
	List<IEdge> getIncomingEdges(String targetNodeId);

	boolean containsNode(String id);

}
