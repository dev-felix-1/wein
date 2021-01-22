package de.fekl.stat.core.api.token;

import de.fekl.dine.util.RandomNames;

public class TokenNames {

	private TokenNames() {

	}

	public static final String TOKEN_NAME_PREFIX = "token_";
	public static final int TOKEN_NAME_LENGTH = 15;

	public static String generateTokenName() {
		return RandomNames.getRandomName(TokenNames.class.getName(), TOKEN_NAME_PREFIX, TOKEN_NAME_LENGTH);
	}

}
