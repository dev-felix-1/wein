package de.fekl.cone.api.core;

import de.fekl.baut.RandomNames;

public class TokenNames {

	private TokenNames() {

	}

	public static String generateTokenName() {
		return RandomNames.getRandomName(TokenNames.class.getName(), "token_", 14);
	}

}
