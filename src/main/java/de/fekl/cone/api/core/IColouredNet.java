package de.fekl.cone.api.core;

import java.util.List;
import java.util.Map;

import de.fekl.dine.api.core.INet;

@Deprecated
public interface IColouredNet {

	String getId();

	INet getNet();

	void putToken(String nodeId, String tokenId, ITokenDeprecated token);

	void removeToken(String nodeId, String tokenId);

	String print();

	Map<String, String> getTokenToNodeMapping();

	Map<String, ITokenDeprecated> getAllToken();

	ITokenDeprecated getToken(String tokenId);
	
	List<String> getTokensOnNode(String nodeId);

}
