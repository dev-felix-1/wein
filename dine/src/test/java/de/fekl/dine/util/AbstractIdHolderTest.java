package de.fekl.dine.util;

import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.base.IIdHolder;

public class AbstractIdHolderTest {

	@Test
	public void test() {
		new IIdHolder<String>() {

			@Override
			public String getId() {
				return null;
			}
		};
	}

}
