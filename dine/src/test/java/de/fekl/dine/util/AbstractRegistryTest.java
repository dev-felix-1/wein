package de.fekl.dine.util;

import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.base.IIdHolder;

public class AbstractRegistryTest {

	@Test
	public void test() {
		new AbstractRegistry<String, IIdHolder<String>>() {
		};
	}

}
