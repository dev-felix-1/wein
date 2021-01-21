package de.fekl.dine.util;

import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.base.IFactory;
import de.fekl.dine.core.api.base.IIdHolder;

public class AbstractBuilderTest {

	@Test
	public <B extends AbstractBuilder<String, IIdHolder<String>, IFactory<String, IIdHolder<String>>, B>> void test() {
		new AbstractBuilder<String, IIdHolder<String>, IFactory<String, IIdHolder<String>>, B>() {

			@Override
			protected IIdHolder<String> doBuild() {
				return null;
			}

		};
	}

}
