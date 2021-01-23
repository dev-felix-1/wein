package de.fekl.dine.core.api.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GraphNamesTest {

	@Test
	public void testGetRandomName() {;

		String nodeName = GraphNames.generateName();
		Assertions.assertNotNull(nodeName);
		Assertions.assertTrue(nodeName.startsWith(GraphNames.PREFIX));
		Assertions.assertEquals(GraphNames.LENGTH, nodeName.length());
	}

	@Test
	public void testQuickCollision() {
		List<String> randomNames = new LinkedList<>();
		for (int i = 0; i < 24000; i++) {
			randomNames.add(GraphNames.generateName());
		}
		Assertions.assertEquals(randomNames.size(), new HashSet<>(randomNames).size());
	}

}
