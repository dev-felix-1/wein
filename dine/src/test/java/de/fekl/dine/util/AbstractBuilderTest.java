package de.fekl.dine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.base.IFactory;
import de.fekl.dine.core.api.base.IIdHolder;
import de.fekl.dine.core.api.base.IRegistry;

public class AbstractBuilderTest {

	@Test
	public <B extends AbstractBuilder<String, IIdHolder<String>, IFactory<String, IIdHolder<String>>, B>> void test() {
		var builder = new AbstractBuilder<String, IIdHolder<String>, IFactory<String, IIdHolder<String>>, B>() {

			@Override
			protected IIdHolder<String> doBuild() {
				return () -> "doBuild";
			}

		};

		var idHolder = builder.build();
		Assertions.assertNotNull(idHolder);
		Assertions.assertEquals("doBuild", idHolder.getId());

		var nodeRegisterHolder = new Object() {
			IIdHolder<String> nodeRegister = null;
		};
		builder.setRegistry(new IRegistry<String, IIdHolder<String>>() {

			@Override
			public void unRegister(String id) {

			}

			@Override
			public void register(IIdHolder<String> node) {
				nodeRegisterHolder.nodeRegister = node;
			}

			@Override
			public IIdHolder<String> get(String id) {
				if (id.equals("doLookup")) {
					return () -> "doLookup";
				}
				return null;
			}

			@Override
			public boolean contains(String id) {
				if (id.equals("doLookup")) {
					return true;
				}
				return false;
			}
		});

		idHolder = builder.id("doLookup").setAutoLookUp(true).build();
		Assertions.assertNotNull(idHolder);
		Assertions.assertEquals("doLookup", idHolder.getId());

		idHolder = builder.id("doSomethingElse").setAutoLookUp(true).build();
		Assertions.assertNotNull(idHolder);
		Assertions.assertEquals("doBuild", idHolder.getId());

		idHolder = builder.id("doLookup").setAutoLookUp(false).build();
		Assertions.assertNotNull(idHolder);
		Assertions.assertEquals("doBuild", idHolder.getId());

		Assertions.assertNull(nodeRegisterHolder.nodeRegister);
		idHolder = builder.id("doBuild").setAutoRegister(true).build();
		Assertions.assertNotNull(idHolder);
		Assertions.assertEquals("doBuild", idHolder.getId());
		Assertions.assertNotNull(nodeRegisterHolder.nodeRegister);
	}

}
