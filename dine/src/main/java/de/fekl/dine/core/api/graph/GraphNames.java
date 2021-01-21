package de.fekl.dine.core.api.graph;

import de.fekl.dine.util.RandomNames;

/**
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 */
public class GraphNames {

	private GraphNames() {

	}

	public static String generateName() {
		return RandomNames.getRandomName(GraphNames.class.getName(), "graph_", 14);
	}

}
