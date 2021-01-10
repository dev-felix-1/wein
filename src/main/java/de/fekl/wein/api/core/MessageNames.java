package de.fekl.wein.api.core;

import de.fekl.baut.RandomNames;

public class MessageNames {

	private MessageNames() {

	}

	public static String generateMessageName() {
		return RandomNames.getRandomName(MessageNames.class.getName(), "msg_", 14);
	}

}
