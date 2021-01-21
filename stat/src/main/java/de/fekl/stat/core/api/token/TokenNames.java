package de.fekl.stat.core.api.token;

import de.fekl.dine.util.RandomNames;

public class TokenNames {

	private TokenNames() {

	}

	public static String generateTokenName() {
		return RandomNames.getRandomName(TokenNames.class.getName(), "token_", 14);
	}

}
