package de.fekl.cone.api.core;

import java.util.List;
import java.util.Map;

import de.fekl.dine.api.core.INet;

public interface IColouredNet {

	String getId();

	INet getNet();

	void putToken(String nodeId, String tokenId, IToken token);

	void removeToken(String nodeId, String tokenId);

	String print();

	Map<String, String> getTokenToNodeMapping();

	Map<String, IToken> getAllToken();

	IToken getToken(String tokenId);
	
	List<String> getTokensOnNode(String nodeId);

}
