package de.fekl.dine.core.api.node;

import de.fekl.dine.util.RandomNames;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 */
public class NodeNames {

	private NodeNames() {

	}

	public static final String PREFIX = "node_";
	public static final int LENGTH = 13;

	public static String generateNodeName() {
		return RandomNames.getRandomName(NodeNames.class.getName(), PREFIX, LENGTH);
	}

}
