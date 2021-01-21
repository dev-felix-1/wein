package de.fekl.tran.impl;

import de.fekl.dine.util.RandomNames;

public class MessageContainerNames {

	private MessageContainerNames() {

	}

	public static String generateName() {
		return RandomNames.getRandomName(MessageContainerNames.class.getName(), "mc_", 14);
	}

}
