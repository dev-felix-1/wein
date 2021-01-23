package de.fekl.dine.core.api.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GraphNamesTest {

	@Test
	public void testGetRandomName() {
		;

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
		// hashCode Collision
		boolean collisionCandidate = (randomNames.size() != new HashSet<>(randomNames).size());
		if (collisionCandidate) {
			// string Collision (real collision)
			while (randomNames.size() > 0) {
				String removed = randomNames.remove(0);
				Assertions.assertFalse(randomNames.stream().anyMatch(name -> name.equals(removed)));
			}
		}
	}

}
