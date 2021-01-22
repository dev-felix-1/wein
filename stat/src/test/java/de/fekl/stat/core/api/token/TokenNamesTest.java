package de.fekl.stat.core.api.token;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenNamesTest {

	@Test
	public void testGetTokenName() {
		String randomName = TokenNames.generateTokenName();
		Assertions.assertNotNull(randomName);
		Assertions.assertTrue(randomName.startsWith(TokenNames.TOKEN_NAME_PREFIX));
		Assertions.assertEquals(TokenNames.TOKEN_NAME_LENGTH, randomName.length());
	}

	@Test
	public void testQuickCollision() {
		List<String> tokenNames = new LinkedList<>();
		for (int i = 0; i < 24000; i++) {
			tokenNames.add(TokenNames.generateTokenName());
		}
		Assertions.assertEquals(tokenNames.size(), new HashSet<>(tokenNames).size());
	}
}
