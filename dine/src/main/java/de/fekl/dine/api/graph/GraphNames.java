package de.fekl.dine.api.graph;

import de.fekl.baut.RandomNames;

public class GraphNames {

	private GraphNames() {

	}

	public static String generateName() {
		return RandomNames.getRandomName(GraphNames.class.getName(), "graph_", 14);
	}

}
