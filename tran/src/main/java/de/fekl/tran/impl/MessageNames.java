package de.fekl.tran.impl;

import de.fekl.dine.util.RandomNames;

public class MessageNames {

	private MessageNames() {

	}

	public static String generateMessageName() {
		return RandomNames.getRandomName(MessageNames.class.getName(), "msg_", 14);
	}

}
