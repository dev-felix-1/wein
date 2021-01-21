package de.fekl.dine.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandomNamesTest {

	@Test
	public void testGetRandomName() {
		String randomName = RandomNames.getRandomName(null, "hello", null);
		Assertions.assertNotNull(randomName);
		Assertions.assertTrue(randomName.startsWith("hello"));
		Assertions.assertEquals(RandomNames.DEFAULT_MAX_LENGTH, randomName.length());

		randomName = RandomNames.getRandomName(null, null, 20);
		Assertions.assertNotNull(randomName);
		Assertions.assertTrue(randomName.startsWith(RandomNames.DEFAULT_PREFIX));
		Assertions.assertEquals(20, randomName.length());

		randomName = RandomNames.getRandomName(null, null, null);
		Assertions.assertNotNull(randomName);
		Assertions.assertTrue(randomName.startsWith(RandomNames.DEFAULT_PREFIX));
		Assertions.assertEquals(RandomNames.DEFAULT_MAX_LENGTH, randomName.length());

		randomName = RandomNames.getRandomName();
		Assertions.assertNotNull(randomName);
		Assertions.assertTrue(randomName.startsWith(RandomNames.DEFAULT_PREFIX));
		Assertions.assertEquals(RandomNames.DEFAULT_MAX_LENGTH, randomName.length());
	}

	@Test
	public void testQuickCollision() {
		List<String> randomNames = new LinkedList<>();
		for (int i = 0; i < 24000; i++) {
			randomNames.add(RandomNames.getRandomName());
		}
		Assertions.assertEquals(randomNames.size(), new HashSet<>(randomNames).size());
	}

}
