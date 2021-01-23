package de.fekl.dine.core.api.node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NodeNamesTest {

	@Test
	public void testGetRandomName() {;

		String nodeName = NodeNames.generateNodeName();
		Assertions.assertNotNull(nodeName);
		Assertions.assertTrue(nodeName.startsWith(NodeNames.PREFIX));
		Assertions.assertEquals(NodeNames.LENGTH, nodeName.length());
	}

	@Test
	public void testQuickCollision() {
		List<String> randomNames = new LinkedList<>();
		for (int i = 0; i < 24000; i++) {
			randomNames.add(NodeNames.generateNodeName());
		}
		Assertions.assertEquals(randomNames.size(), new HashSet<>(randomNames).size());
	}

}
