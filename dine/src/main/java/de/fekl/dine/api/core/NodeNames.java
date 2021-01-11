package de.fekl.dine.api.core;

import de.fekl.baut.RandomNames;

public class NodeNames {
	
	private NodeNames() {
		
	}
	
	public static String generateNodeName() {
		return RandomNames.getRandomName(NodeNames.class.getName(), "node_", 13);
	}

}
