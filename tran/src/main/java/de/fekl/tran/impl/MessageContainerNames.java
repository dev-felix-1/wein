package de.fekl.tran.impl;

import de.fekl.baut.RandomNames;

public class MessageContainerNames {

	private MessageContainerNames() {

	}

	public static String generateName() {
		return RandomNames.getRandomName(MessageContainerNames.class.getName(), "mc_", 14);
	}

}
